/* ============================================================
   Voyager Tour Assistant — drop-in chatbot widget
   Reads live travel packages from /api/trips (TripController)
   No dependencies. Include with:
   <script src="/js/chatbot-widget.js" defer></script>
   ============================================================ */
(function(){
  if(document.getElementById('vy-toggle')) return; // avoid double-init

  /* ---------------- Fonts + Styles ---------------- */
  var fontLink = document.createElement('link');
  fontLink.rel = 'stylesheet';
  fontLink.href = 'https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@500;600;700&family=Inter:wght@400;500;600&display=swap';
  document.head.appendChild(fontLink);

  var style = document.createElement('style');
  style.id = 'vy-chatbot-styles';
  style.textContent = `
  #vy-toggle,#vy-panel,#vy-backdrop,#vy-panel *{box-sizing:border-box;}
  #vy-toggle{
    --teal:#2DD4BF; --teal-dark:#0E7C74; --coral:#FF6F59; --coral-dark:#D9503F; --amber:#F5C94D;
    --bg-deep:#0A1F2B; --bg-panel:#0F2B38; --bg-panel-2:#123543;
    --text-light:#EAF4F2; --text-muted:#87A9AC; --line:rgba(45,212,191,0.18);
    position:fixed; right:32px; bottom:32px; width:64px; height:64px; border-radius:50%;
    border:none; cursor:pointer; z-index:99999;
    background:conic-gradient(from 180deg, var(--teal), var(--coral), var(--amber), var(--teal));
    padding:3px; display:flex; align-items:center; justify-content:center;
    box-shadow:0 8px 24px rgba(0,0,0,0.45); transition:transform .25s ease;
  }
  #vy-toggle:hover{transform:scale(1.06) rotate(6deg);}
  #vy-toggle .vy-inner{width:100%;height:100%;border-radius:50%;background:var(--bg-panel);display:flex;align-items:center;justify-content:center;}
  #vy-toggle svg{width:26px;height:26px;stroke:var(--text-light);}
  #vy-toggle .vy-needle{transform-origin:32px 32px;animation:vy-spin 6s linear infinite;}
  @keyframes vy-spin{to{transform:rotate(360deg);}}

  #vy-backdrop{
    position:fixed; inset:0; background:rgba(5,15,20,0.55); backdrop-filter:blur(3px);
    opacity:0; pointer-events:none; transition:opacity .25s ease; z-index:99997;
  }
  #vy-backdrop.open{opacity:1; pointer-events:auto;}

  #vy-panel{
    --teal:#2DD4BF; --teal-dark:#0E7C74; --coral:#FF6F59; --coral-dark:#D9503F; --amber:#F5C94D;
    --bg-deep:#0A1F2B; --bg-panel:#0F2B38; --bg-panel-2:#123543;
    --text-light:#EAF4F2; --text-muted:#87A9AC; --line:rgba(45,212,191,0.18);
    position:fixed; top:50%; left:50%; width:560px; max-width:calc(100vw - 32px);
    height:780px; max-height:calc(100vh - 56px);
    background:var(--bg-panel); border-radius:22px; border:1px solid var(--line);
    box-shadow:0 30px 80px rgba(0,0,0,0.6); display:flex; flex-direction:column; overflow:hidden;
    opacity:0; transform:translate(-50%,-50%) scale(.95); pointer-events:none;
    transition:opacity .25s ease, transform .25s ease; z-index:99998;
    font-family:'Inter',sans-serif;
  }
  #vy-panel.open{opacity:1; transform:translate(-50%,-50%) scale(1); pointer-events:auto;}

  #vy-header{position:relative;padding:20px 20px 26px;overflow:hidden;flex-shrink:0;
    background:
      radial-gradient(circle at 15% -10%, rgba(255,111,89,0.35) 0%, transparent 55%),
      radial-gradient(circle at 100% 120%, rgba(245,201,77,0.25) 0%, transparent 50%),
      linear-gradient(135deg, var(--teal-dark), #0a5f5a 55%, #0a3b45);
  }
  #vy-header .vy-skyline{position:absolute;left:0;right:0;bottom:-1px;height:38px;opacity:.5;pointer-events:none;}
  #vy-header .vy-contour{position:absolute;inset:0;opacity:.25;pointer-events:none;}
  #vy-header-top{display:flex;align-items:center;gap:10px;position:relative;z-index:2;}
  #vy-header-top .vy-badge{width:38px;height:38px;border-radius:11px;background:rgba(255,255,255,0.12);display:flex;align-items:center;justify-content:center;flex-shrink:0;}
  #vy-header-top .vy-badge svg{width:20px;height:20px;stroke:var(--amber);}
  #vy-title{font-family:'Space Grotesk',sans-serif;font-weight:700;font-size:16.5px;color:#fff;margin:0;letter-spacing:.2px;}
  #vy-subtitle{font-size:12px;color:#CFEDE9;margin:2px 0 0;display:flex;align-items:center;gap:5px;}
  #vy-subtitle .vy-status-dot{width:6px;height:6px;border-radius:50%;background:#5CFFCE;box-shadow:0 0 6px #5CFFCE;}
  #vy-close{margin-left:auto;background:transparent;border:none;cursor:pointer;width:28px;height:28px;display:flex;align-items:center;justify-content:center;border-radius:8px;position:relative;z-index:2;}
  #vy-close:hover{background:rgba(255,255,255,0.12);}
  #vy-close svg{width:16px;height:16px;stroke:#fff;}

  .vy-flightpath{position:relative;height:20px;margin-top:14px;z-index:2;}
  .vy-flightpath svg{width:100%;height:20px;display:block;}
  .vy-flightpath .vy-plane{animation:vy-fly 4.5s ease-in-out infinite;}
  @keyframes vy-fly{0%{transform:translate(0px,0px) rotate(0deg);}50%{transform:translate(150px,-6px) rotate(8deg);}100%{transform:translate(300px,0px) rotate(0deg);}}

  #vy-chips{display:flex;gap:8px;padding:12px 14px;overflow-x:auto;background:var(--bg-panel-2);flex-shrink:0;border-bottom:1px solid var(--line);scrollbar-width:thin;}
  #vy-chips::-webkit-scrollbar{height:5px;}
  #vy-chips::-webkit-scrollbar-thumb{background:var(--teal-dark);border-radius:10px;}
  .vy-chip{flex-shrink:0;display:flex;align-items:center;gap:6px;padding:7px 12px;border-radius:100px;background:rgba(45,212,191,0.08);border:1px solid var(--line);color:var(--text-light);font-size:12px;font-weight:500;cursor:pointer;transition:all .18s ease;white-space:nowrap;}
  .vy-chip:hover{background:rgba(45,212,191,0.18);border-color:var(--teal);transform:translateY(-1px);}
  .vy-chip svg{width:14px;height:14px;stroke:var(--teal);flex-shrink:0;}

  #vy-messages{flex:1;overflow-y:auto;padding:18px 16px;display:flex;flex-direction:column;gap:14px;position:relative;
    background:radial-gradient(circle at 8% 8%, rgba(45,212,191,0.10) 0%, transparent 32%),
               radial-gradient(circle at 92% 88%, rgba(255,111,89,0.10) 0%, transparent 32%),
               var(--bg-deep);}
  #vy-messages::-webkit-scrollbar{width:6px;}
  #vy-messages::-webkit-scrollbar-thumb{background:var(--bg-panel-2);border-radius:10px;}
  #vy-bg-layer{position:absolute;inset:0;z-index:0;pointer-events:none;opacity:.9;}
  #vy-bg-layer svg{width:100%;height:100%;}

  .vy-msg-row{position:relative;z-index:1;display:flex;gap:8px;max-width:88%;}
  .vy-msg-row.user{align-self:flex-end;flex-direction:row-reverse;}
  .vy-avatar{width:26px;height:26px;border-radius:50%;flex-shrink:0;display:flex;align-items:center;justify-content:center;background:var(--bg-panel-2);border:1px solid var(--line);}
  .vy-avatar svg{width:14px;height:14px;}
  .vy-msg-row.bot .vy-avatar svg{stroke:var(--amber);}
  .vy-msg-row.user .vy-avatar svg{stroke:var(--coral);}

  .vy-bubble{padding:10px 13px;font-size:13.5px;line-height:1.5;color:var(--text-light);position:relative;}
  .vy-msg-row.bot .vy-bubble{background:rgba(15,43,56,0.85);border:1.5px dashed var(--teal-dark);border-radius:4px 16px 16px 16px;}
  .vy-msg-row.bot .vy-bubble::before{content:'';position:absolute;top:-5px;right:-5px;width:16px;height:16px;border:1.5px dashed var(--amber);border-radius:50%;background:var(--bg-panel);}
  .vy-msg-row.user .vy-bubble{background:linear-gradient(135deg, var(--coral), var(--coral-dark));border-radius:16px 4px 16px 16px;color:#fff;}
  .vy-bubble b{color:var(--amber);}
  .vy-msg-row.user .vy-bubble b{color:#FFE1D8;}
  .vy-bubble ul{margin:6px 0 0;padding-left:16px;}
  .vy-bubble li{margin-bottom:5px;}
  .vy-bubble a.vy-link-btn{display:inline-block;margin-top:8px;padding:6px 12px;border-radius:100px;background:var(--teal);color:#062522;text-decoration:none;font-weight:600;font-size:12.5px;}
  .vy-bubble a.vy-link-btn:hover{background:#57e6d4;}
  .vy-bubble a.vy-text-link{color:var(--teal);font-weight:600;text-decoration:underline;}

  .vy-typing .vy-bubble{display:flex;gap:4px;align-items:center;padding:12px 14px;}
  .vy-typing .vy-dot{width:6px;height:6px;border-radius:50%;background:var(--teal);animation:vy-bounce 1.2s infinite;}
  .vy-typing .vy-dot:nth-child(2){animation-delay:.15s;}
  .vy-typing .vy-dot:nth-child(3){animation-delay:.3s;}
  @keyframes vy-bounce{0%,60%,100%{transform:translateY(0);opacity:.5;}30%{transform:translateY(-4px);opacity:1;}}

  #vy-inputbar{display:flex;align-items:center;gap:8px;padding:14px 12px;background:var(--bg-panel-2);border-top:1.5px dashed var(--line);position:relative;flex-shrink:0;}
  #vy-inputbar::before{content:'';position:absolute;top:-6px;left:-6px;width:12px;height:12px;border-radius:50%;background:var(--bg-deep);}
  #vy-inputbar::after{content:'';position:absolute;top:-6px;right:-6px;width:12px;height:12px;border-radius:50%;background:var(--bg-deep);}
  #vy-input{flex:1;background:var(--bg-deep);border:1px solid var(--line);border-radius:100px;padding:11px 16px;color:var(--text-light);font-family:'Inter',sans-serif;font-size:13.5px;outline:none;}
  #vy-input::placeholder{color:var(--text-muted);}
  #vy-input:focus{border-color:var(--teal);}
  #vy-send{width:40px;height:40px;border-radius:50%;flex-shrink:0;border:none;cursor:pointer;background:linear-gradient(135deg, var(--teal), var(--teal-dark));display:flex;align-items:center;justify-content:center;transition:transform .15s ease;}
  #vy-send:hover{transform:scale(1.08);}
  #vy-send svg{width:17px;height:17px;stroke:#062522;}

  @media (max-width:600px){
    #vy-panel{width:calc(100vw - 24px);height:calc(100vh - 40px);}
    #vy-toggle{right:20px;bottom:20px;}
  }
  `;
  document.head.appendChild(style);

  /* ---------------- Markup ---------------- */
  var toggle = document.createElement('button');
  toggle.id = 'vy-toggle';
  toggle.setAttribute('aria-label','Open tour assistant');
  toggle.innerHTML = `
    <div class="vy-inner">
      <svg class="vy-needle" viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg">
        <circle cx="32" cy="32" r="20" stroke-width="2"/>
        <path d="M32 18 L37 32 L32 46 L27 32 Z" fill="none" stroke-width="2"/>
      </svg>
    </div>`;

  var backdrop = document.createElement('div');
  backdrop.id = 'vy-backdrop';

  var panel = document.createElement('div');
  panel.id = 'vy-panel';
  panel.innerHTML = `
    <div id="vy-header">
      <svg class="vy-contour" viewBox="0 0 380 100" xmlns="http://www.w3.org/2000/svg">
        <path d="M-10 20 Q 90 60 190 20 T 390 20" stroke="#fff" stroke-width="1" fill="none"/>
        <path d="M-10 45 Q 90 85 190 45 T 390 45" stroke="#fff" stroke-width="1" fill="none"/>
        <path d="M-10 70 Q 90 30 190 70 T 390 70" stroke="#fff" stroke-width="1" fill="none"/>
      </svg>
      <svg class="vy-skyline" viewBox="0 0 400 40" preserveAspectRatio="none" xmlns="http://www.w3.org/2000/svg">
        <rect x="0" y="18" width="14" height="22" fill="#08202a"/><rect x="16" y="10" width="10" height="30" fill="#08202a"/>
        <rect x="28" y="22" width="16" height="18" fill="#08202a"/>
        <path d="M44 22 L52 22 L52 14 L56 14 L56 22 L64 22 L64 40 L44 40 Z" fill="#08202a"/>
        <rect x="70" y="16" width="12" height="24" fill="#08202a"/><rect x="86" y="24" width="20" height="16" fill="#08202a"/>
        <rect x="300" y="20" width="14" height="20" fill="#08202a"/><rect x="318" y="8" width="10" height="32" fill="#08202a"/>
        <path d="M332 24 L340 24 L340 16 L344 16 L344 24 L352 24 L352 40 L332 40 Z" fill="#08202a"/>
        <rect x="358" y="18" width="16" height="22" fill="#08202a"/>
        <rect x="150" y="26" width="100" height="14" fill="#08202a" opacity=".5"/>
      </svg>
      <div id="vy-header-top">
        <div class="vy-badge">
          <svg viewBox="0 0 24 24" fill="none" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 2l2.4 6.8L21 11l-6.6 2.2L12 20l-2.4-6.8L3 11l6.6-2.2z"/></svg>
        </div>
        <div>
          <p id="vy-title">Voyager Assistant</p>
          <p id="vy-subtitle"><span class="vy-status-dot"></span>Online · Travel Ease AI</p>
        </div>
        <button id="vy-close" aria-label="Close chat">
          <svg viewBox="0 0 24 24" fill="none" stroke-width="2" stroke-linecap="round"><path d="M18 6 6 18M6 6l12 12"/></svg>
        </button>
      </div>
      <div class="vy-flightpath">
        <svg viewBox="0 0 340 20"><path d="M0 10 Q 170 -6 340 10" stroke="#ffffff55" stroke-width="1.5" stroke-dasharray="4 5" fill="none"/></svg>
        <svg class="vy-plane" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#F5C94D" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="position:absolute;top:-3px;left:0;">
          <path d="M2 12 12 3l1 4 8-1-6 6 6 6-8-1-1 4z"/>
        </svg>
      </div>
    </div>

    <div id="vy-chips">
      <div class="vy-chip" data-q="Show me popular destinations">
        <svg viewBox="0 0 24 24" fill="none" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 21s7-6.5 7-12a7 7 0 1 0-14 0c0 5.5 7 12 7 12z"/><circle cx="12" cy="9" r="2.5"/></svg>
        Destinations
      </div>
      <div class="vy-chip" data-q="I want to book a tour">
        <svg viewBox="0 0 24 24" fill="none" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 10a2 2 0 0 0 0 4v3a1 1 0 0 0 1 1h16a1 1 0 0 0 1-1v-3a2 2 0 0 1 0-4V6a1 1 0 0 0-1-1H4a1 1 0 0 0-1 1z"/><path d="M13 5v14" stroke-dasharray="2 3"/></svg>
        Book a Tour
      </div>
      <div class="vy-chip" data-q="What are your tour packages and pricing">
        <svg viewBox="0 0 24 24" fill="none" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M20.6 12.3 12.7 20a2 2 0 0 1-2.8 0l-7-7a2 2 0 0 1 0-2.8L10.8 2.4a2 2 0 0 1 1.4-.6H19a2 2 0 0 1 2 2v6.1a2 2 0 0 1-.4 1.4z"/><circle cx="15.5" cy="7.5" r="1.5"/></svg>
        Pricing
      </div>
      <div class="vy-chip" data-q="Check my booking status">
        <svg viewBox="0 0 24 24" fill="none" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="4" width="18" height="17" rx="2"/><path d="M16 2v4M8 2v4M3 10h18"/></svg>
        My Booking
      </div>
      <div class="vy-chip" data-q="How is the weather for the trip">
        <svg viewBox="0 0 24 24" fill="none" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17.5 19a4.5 4.5 0 0 0 0-9 6 6 0 0 0-11.4-2.4A5 5 0 0 0 6.5 17"/></svg>
        Weather
      </div>
    </div>

    <div id="vy-messages">
      <div id="vy-bg-layer">
        <svg viewBox="0 0 500 640" xmlns="http://www.w3.org/2000/svg">
          <g fill="#2DD4BF" opacity="0.35">
            <circle cx="60" cy="90" r="2.4"/><circle cx="78" cy="80" r="1.8"/><circle cx="72" cy="105" r="1.6"/>
            <circle cx="95" cy="70" r="2"/><circle cx="110" cy="95" r="1.6"/>
            <circle cx="150" cy="130" r="2.2"/><circle cx="168" cy="118" r="1.6"/><circle cx="180" cy="140" r="1.8"/>
            <circle cx="230" cy="80" r="2"/><circle cx="250" cy="95" r="1.6"/><circle cx="265" cy="75" r="1.8"/>
            <circle cx="320" cy="130" r="2.2"/><circle cx="340" cy="150" r="1.6"/><circle cx="355" cy="120" r="1.8"/>
            <circle cx="410" cy="95" r="2"/><circle cx="430" cy="115" r="1.6"/>
            <circle cx="90" cy="330" r="2"/><circle cx="110" cy="350" r="1.6"/><circle cx="70" cy="360" r="1.8"/>
            <circle cx="220" cy="300" r="2.2"/><circle cx="240" cy="320" r="1.6"/>
            <circle cx="330" cy="340" r="2"/><circle cx="350" cy="360" r="1.6"/><circle cx="365" cy="330" r="1.8"/>
            <circle cx="420" cy="300" r="2"/><circle cx="60" cy="520" r="2"/><circle cx="80" cy="540" r="1.6"/>
            <circle cx="200" cy="510" r="2.2"/><circle cx="220" cy="530" r="1.6"/>
            <circle cx="340" cy="520" r="2"/><circle cx="360" cy="540" r="1.6"/><circle cx="400" cy="510" r="1.8"/>
          </g>
          <g stroke="#FF6F59" stroke-width="1.2" stroke-dasharray="1 6" stroke-linecap="round" fill="none" opacity="0.55">
            <path d="M78 80 Q 160 40 250 95"/><path d="M250 95 Q 330 60 410 95"/>
            <path d="M110 350 Q 200 260 330 340"/><path d="M240 320 Q 300 420 360 540"/>
            <path d="M80 540 Q 150 470 220 530"/>
          </g>
          <g fill="#F5C94D" opacity="0.7">
            <circle cx="250" cy="95" r="3"/><circle cx="330" cy="340" r="3"/><circle cx="220" cy="530" r="3"/>
          </g>
          <g stroke="#EAF4F2" stroke-width="1.4" fill="none" opacity="0.05" stroke-linecap="round" stroke-linejoin="round">
            <g transform="translate(-30,470) scale(3.2)"><rect x="6" y="9" width="12" height="10" rx="1.5"/><path d="M9 9V6a1 1 0 0 1 1-1h4a1 1 0 0 1 1 1v3"/></g>
            <g transform="translate(360,-30) scale(3.4)"><path d="M2 12 12 3l1 4 8-1-6 6 6 6-8-1-1 4z"/></g>
            <g transform="translate(300,470) scale(3)"><circle cx="12" cy="12" r="9"/><path d="M12 3a13 13 0 0 1 0 18M12 3a13 13 0 0 0 0 18M3 12h18"/></g>
          </g>
        </svg>
      </div>
    </div>

    <div id="vy-inputbar">
      <input id="vy-input" type="text" placeholder="Ask about tours, prices, dates…" autocomplete="off" />
      <button id="vy-send" aria-label="Send message">
        <svg viewBox="0 0 24 24" fill="none" stroke-linecap="round" stroke-linejoin="round" stroke-width="2.2"><path d="M22 2 11 13"/><path d="M22 2 15 22l-4-9-9-4z"/></svg>
      </button>
    </div>
  `;

  document.body.appendChild(backdrop);
  document.body.appendChild(panel);
  document.body.appendChild(toggle);

  /* ---------------- Behaviour ---------------- */
  var ICONS = {
    bot: '<path d="M12 2l2.4 6.8L21 11l-6.6 2.2L12 20l-2.4-6.8L3 11l6.6-2.2z"/>',
    user: '<circle cx="12" cy="8" r="4"/><path d="M4 21c0-4.4 3.6-7 8-7s8 2.6 8 7"/>'
  };

  var messagesEl = document.getElementById('vy-messages');
  var inputEl = document.getElementById('vy-input');

  function openChat(){ panel.classList.add('open'); backdrop.classList.add('open'); inputEl.focus(); }
  function closeChat(){ panel.classList.remove('open'); backdrop.classList.remove('open'); }

  toggle.addEventListener('click', openChat);
  document.getElementById('vy-close').addEventListener('click', closeChat);
  backdrop.addEventListener('click', closeChat);
  document.addEventListener('keydown', function(e){ if(e.key === 'Escape') closeChat(); });

  function addMessage(html, sender){
    var row = document.createElement('div');
    row.className = 'vy-msg-row ' + sender;
    row.innerHTML =
      '<div class="vy-avatar"><svg viewBox="0 0 24 24" fill="none" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">' +
      (sender === 'bot' ? ICONS.bot : ICONS.user) + '</svg></div>' +
      '<div class="vy-bubble">' + html + '</div>';
    messagesEl.appendChild(row);
    messagesEl.scrollTop = messagesEl.scrollHeight;
  }

  function showTyping(){
    var row = document.createElement('div');
    row.className = 'vy-msg-row bot vy-typing';
    row.id = 'vy-typing';
    row.innerHTML =
      '<div class="vy-avatar"><svg viewBox="0 0 24 24" fill="none" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">' + ICONS.bot + '</svg></div>' +
      '<div class="vy-bubble"><span class="vy-dot"></span><span class="vy-dot"></span><span class="vy-dot"></span></div>';
    messagesEl.appendChild(row);
    messagesEl.scrollTop = messagesEl.scrollHeight;
  }
  function hideTyping(){ var t = document.getElementById('vy-typing'); if(t) t.remove(); }

  function escapeHtml(str){
    var d = document.createElement('div');
    d.innerText = str;
    return d.innerHTML;
  }
  function money(n){
    return '₹' + Number(n).toLocaleString('en-IN', {maximumFractionDigits:0});
  }

  /* ---------------- Live trip package data ---------------- */
  var TRIPS = [];
  var tripsLoaded = false;
  fetch('/api/trips').then(function(r){ return r.ok ? r.json() : []; })
    .then(function(data){ TRIPS = Array.isArray(data) ? data : []; tripsLoaded = true; })
    .catch(function(){ tripsLoaded = true; });

  function findTrip(text){
    var lower = text.toLowerCase();
    var match = null, matchLen = 0;
    TRIPS.forEach(function(t){
      [t.destination, t.packageName, t.country].forEach(function(field){
        if(field){
          var f = field.toLowerCase();
          if(lower.indexOf(f) !== -1 && f.length > matchLen){ match = t; matchLen = f.length; }
        }
      });
    });
    return match;
  }

  function tripCard(t){
    return '<b>' + escapeHtml(t.packageName) + '</b> — ' + escapeHtml(t.destination) + ', ' + escapeHtml(t.country) +
      '<br>' + t.durationDays + ' days · ' + money(t.price) + ' per person' +
      (t.rating ? ' · ⭐ ' + t.rating : '') +
      '<br><a class="vy-link-btn" href="/trip/' + t.id + '">View Package</a>';
  }

  function destinationsReply(){
    if(!tripsLoaded) return "Let me pull up our current packages — one moment…";
    if(!TRIPS.length) return "I couldn't find any live packages right now — take a look at our <a class=\"vy-text-link\" href=\"/trips\">full trips page</a> instead.";
    var top = TRIPS.slice(0, 5);
    var html = "Here are some packages available right now:<ul>" +
      top.map(function(t){ return '<li>' + tripCard(t) + '</li>'; }).join('') +
      '</ul><a class="vy-text-link" href="/trips">See all packages →</a>';
    return html;
  }

  function pricingReply(dest){
    if(!tripsLoaded) return "Checking current pricing — one moment…";
    if(dest) return "Here's the pricing for that package:<br>" + tripCard(dest);
    if(!TRIPS.length) return "Pricing varies by package — check the <a class=\"vy-text-link\" href=\"/trips\">trips page</a> for current rates.";
    var prices = TRIPS.map(function(t){ return t.price; });
    var min = Math.min.apply(null, prices), max = Math.max.apply(null, prices);
    return "Our packages currently range from <b>" + money(min) + "</b> to <b>" + money(max) + "</b> per person, depending on destination and duration.<br>Tell me a destination and I'll give you the exact price!";
  }

  function bookingReply(dest){
    if(dest){
      return "Great choice — <b>" + escapeHtml(dest.packageName) + "</b>!<br>" +
        dest.durationDays + " days in " + escapeHtml(dest.destination) + " for " + money(dest.price) + " per person." +
        '<br><a class="vy-link-btn" href="/trip/' + dest.id + '">Book This Package</a>';
    }
    return "I'd love to help you book! Tell me a destination you're interested in, or " +
      '<a class="vy-text-link" href="/trips">browse all packages</a> and pick one to book directly.';
  }

  function itineraryReply(dest){
    if(dest){
      var days = dest.durationDays || 5;
      var lines = ['Arrival & check-in', 'Guided sightseeing', 'Adventure/leisure activity', 'Local culture & cuisine', 'Free day / optional add-ons', 'Shopping', 'Departure'];
      var html = "Sample itinerary for <b>" + escapeHtml(dest.packageName) + "</b> (" + days + " days):<ul>";
      for(var i=0;i<Math.min(days, lines.length);i++){
        html += '<li><b>Day ' + (i+1) + '</b> — ' + lines[i] + '</li>';
      }
      html += '</ul><a class="vy-link-btn" href="/trip/' + dest.id + '">View Full Details</a>';
      return html;
    }
    return "Sample 5-day itinerary:<ul><li><b>Day 1</b> — Arrival & local sightseeing</li><li><b>Day 2</b> — Guided city/nature tour</li><li><b>Day 3</b> — Adventure activity or excursion</li><li><b>Day 4</b> — Leisure day / optional add-ons</li><li><b>Day 5</b> — Shopping & departure</li></ul>Tell me a destination for a custom itinerary!";
  }

  var INTENTS = [
    { name:'greeting', keywords:['hi','hello','hey','good morning','good evening','namaste'],
      respond: function(){ return "Hey there! 👋 I'm your Voyager travel assistant. I can help with destinations, bookings, pricing, itineraries and more — what are you planning?"; } },
    { name:'destinations', keywords:['destination','place','where can','recommend','suggest a trip','location','visit','popular'],
      respond: function(){ return destinationsReply(); } },
    { name:'booking', keywords:['book','reserve','booking','reservation','package','sign up','join a tour'],
      respond: function(text){ return bookingReply(findTrip(text)); } },
    { name:'pricing', keywords:['price','cost','fee','budget','how much','charges','rate'],
      respond: function(text){ return pricingReply(findTrip(text)); } },
    { name:'itinerary', keywords:['itinerary','schedule','day plan','day-wise','plan my trip','agenda'],
      respond: function(text){ return itineraryReply(findTrip(text)); } },
    { name:'booking_status', keywords:['my booking','status','confirm','ticket status','pnr','track my'],
      respond: function(){ return 'You can check all your bookings anytime here: <a class="vy-link-btn" href="/my-bookings">My Bookings</a>'; } },
    { name:'weather', keywords:['weather','climate','season','rain','temperature','best time'],
      respond: function(){ return "🌦️ Weather depends on the destination and month. Generally:<ul><li>Hill stations — best <b>Mar–Jun</b> & <b>Sep–Nov</b></li><li>Beaches — best <b>Nov–Feb</b></li><li>Desert regions — avoid <b>peak summer</b></li></ul>Tell me your destination and travel month for a precise forecast."; } },
    { name:'cancellation', keywords:['cancel','refund','reschedule','change date'],
      respond: function(){ return "Our cancellation policy:<ul><li>Free cancellation up to 7 days before departure</li><li>50% refund within 3–7 days</li><li>Rescheduling is free once, subject to availability</li></ul>Want me to point you to your booking to start that request?"; } },
    { name:'payment', keywords:['pay','payment','upi','card','emi','wallet','transaction'],
      respond: function(){ return "We accept UPI, credit/debit cards, and net banking. A 20% advance confirms your booking, with the balance due 7 days before departure."; } },
    { name:'support', keywords:['help','support','agent','human','talk to someone','contact','problem','issue'],
      respond: function(){ return "I've got you 🙌 — I can answer most travel questions right here. For anything more complex, our support team is reachable at <b>support@travelease.com</b>."; } },
    { name:'thanks', keywords:['thank','thanks','great','awesome','perfect','cool'],
      respond: function(){ return "You're welcome! Safe travels ✈️ — ping me anytime you're planning your next trip."; } }
  ];

  function classify(raw){
    var text = raw.toLowerCase().trim();
    var best = null, bestScore = 0;
    INTENTS.forEach(function(intent){
      var score = 0;
      intent.keywords.forEach(function(k){ if(text.indexOf(k) !== -1) score += k.split(' ').length; });
      if(score > bestScore){ bestScore = score; best = intent; }
    });
    var dest = findTrip(text);
    if(best) return best.respond(text);
    if(dest) return "<b>" + escapeHtml(dest.packageName) + "</b> in " + escapeHtml(dest.destination) + " looks great! Want pricing, itinerary, or to book it?" +
      '<br><a class="vy-link-btn" href="/trip/' + dest.id + '">View Package</a>';
    return "I didn't quite catch that 🤔 — you can ask me about <b>destinations</b>, <b>bookings</b>, <b>pricing</b>, <b>itineraries</b>, <b>weather</b>, or your <b>booking status</b>.";
  }

  function handleUserText(text){
    if(!text.trim()) return;
    addMessage(escapeHtml(text), 'user');
    inputEl.value = '';
    showTyping();
    var delay = 500 + Math.random()*500;
    setTimeout(function(){
      hideTyping();
      addMessage(classify(text), 'bot');
    }, delay);
  }

  document.getElementById('vy-send').addEventListener('click', function(){ handleUserText(inputEl.value); });
  inputEl.addEventListener('keydown', function(e){ if(e.key === 'Enter') handleUserText(inputEl.value); });
  document.querySelectorAll('.vy-chip').forEach(function(chip){
    chip.addEventListener('click', function(){ handleUserText(chip.dataset.q); });
  });

  addMessage("Namaste! 🧭 I'm the Voyager Assistant for Travel Ease, here to help you explore destinations, plan itineraries, and book your next tour. How can I help today?", 'bot');
})();
