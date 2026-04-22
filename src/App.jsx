import React, { useState, useMemo, useRef, useEffect } from 'react';
import {
  Play, Pause, SkipForward, SkipBack, Heart,
  User, Music, Zap, Coffee, CloudRain, RotateCcw
} from 'lucide-react';

// Safe fallback to avoid blank-screen runtime issues from animation libs.
const motion = { div: 'div', button: 'button' };

const USERS_STORAGE_KEY = 'moodsync_users';
const MOOD_OPTIONS = [
  { id: 'Happy', icon: <Zap size={18} /> },
  { id: 'Calm', icon: <Coffee size={18} /> },
  { id: 'Sad', icon: <CloudRain size={18} /> }
];
const LANGUAGE_FILTERS = ['All', 'Hindi', 'English'];
const q = (question, answers) => ({ q: question, a: answers.map(([text, mood]) => ({ text, mood })) });

const loadUsers = () => {
  try {
    const users = JSON.parse(localStorage.getItem(USERS_STORAGE_KEY) || '[]');
    return Array.isArray(users) ? users : [];
  } catch {
    return [];
  }
};

const saveUsers = (users) => {
  localStorage.setItem(USERS_STORAGE_KEY, JSON.stringify(users));
};

const QUESTIONS = [
  q("What lighting matches your current energy?", [["Bright and Sunlight", "Happy"], ["Soft and Cozy", "Calm"], ["Dim and Moody", "Sad"]]),
  q("Pick an environment to teleport to right now.", [["Sunlit Beach", "Happy"], ["Quiet Library", "Calm"], ["Rainy Coffee Shop", "Sad"]]),
  q("How would you describe your current pace?", [["Racing and Excited", "Happy"], ["Steady and Flowing", "Calm"], ["Slow and Pensive", "Sad"]]),
  q("If your mood was a color, what would it be?", [["Electric Yellow", "Happy"], ["Deep Ocean Blue", "Calm"], ["Misty Charcoal", "Sad"]]),
  q("What activity sounds most appealing?", [["Festival / Party", "Happy"], ["Stargazing", "Calm"], ["Journaling", "Sad"]])
];

const LoginView = ({ onLogin }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [mode, setMode] = useState('login');
  const [message, setMessage] = useState('');
  const toggleMode = () => {
    setMode(mode === 'login' ? 'signup' : 'login');
    setMessage('');
  };

  const handleAuth = () => {
    const normalizedEmail = email.trim().toLowerCase();
    const trimmedPassword = password.trim();

    if (!normalizedEmail || !trimmedPassword) {
      setMessage('Please fill in email and password.');
      return;
    }

    const users = loadUsers();
    const existingUser = users.find(u => u.email === normalizedEmail);

    if (mode === 'signup') {
      if (existingUser) {
        setMessage('Account already exists. Please log in.');
        setMode('login');
        return;
      }

      const updatedUsers = [...users, { email: normalizedEmail, password: trimmedPassword }];
      saveUsers(updatedUsers);
      setMessage('Account created! You can log in now.');
      setMode('login');
      setPassword('');
      return;
    }

    if (!existingUser || existingUser.password !== trimmedPassword) {
      setMessage('Invalid email or password.');
      return;
    }

    setMessage('');
    onLogin(normalizedEmail.split('@')[0] || 'User');
  };

  return (
    <motion.div
      initial={{ opacity: 0, y: 30 }}
      animate={{ opacity: 1, y: 0 }}
      className="premium-card red-glow-hover"
      style={{ width: '400px' }}
    >
      <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
        <h1 style={{ color: 'var(--primary)', fontSize: '2.5rem', marginBottom: '0.5rem' }}>MoodSync</h1>
        <p style={{ color: "var(--text-dim)" }}>Harmony in every beat.</p>
      </div>
      <div className="input-group">
        <label>Email Address</label>
        <input type="email" placeholder="you@example.com" value={email} onChange={e => setEmail(e.target.value)} />
      </div>
      <div className="input-group">
        <label>Password</label>
        <input type="password" placeholder="••••••••" value={password} onChange={e => setPassword(e.target.value)} />
      </div>
      {!!message && (
        <p style={{ fontSize: '0.85rem', color: 'var(--primary)', marginBottom: '1rem' }}>{message}</p>
      )}
      <button className="btn-primary" style={{ width: '100%' }} onClick={handleAuth}>
        {mode === 'login' ? 'Login' : 'Sign up'} <Zap size={18} />
      </button>
      <p style={{ textAlign: 'center', marginTop: '1.5rem', fontSize: '0.8rem', color: "var(--text-dim)" }}>
        {mode === 'login' ? "Don't have an account? " : "Already have an account? "}
        <span
          style={{ color: 'var(--primary)', cursor: 'pointer' }}
          onClick={toggleMode}
        >
          {mode === 'login' ? 'Sign up' : 'Login'}
        </span>
      </p>
    </motion.div>
  );
};

const QuizView = ({ onComplete }) => {
  const [currentIdx, setCurrentIdx] = useState(0);
  const [scores, setScores] = useState({ Happy: 0, Calm: 0, Sad: 0 });

  const handleAnswer = (mood) => {
    const newScores = { ...scores, [mood]: scores[mood] + 1 };
    setScores(newScores);
    if (currentIdx < QUESTIONS.length - 1) {
      setCurrentIdx(currentIdx + 1);
    } else {
      const winner = Object.keys(newScores).reduce((a, b) => newScores[a] > newScores[b] ? a : b);
      onComplete(winner);
    }
  };

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      className="premium-card"
      style={{ width: '600px', textAlign: 'center' }}
    >
      <p style={{ color: 'var(--primary)', fontWeight: '700', marginBottom: '1rem' }}>
        QUESTION {currentIdx + 1} OF {QUESTIONS.length}
      </p>
      <h2 style={{ fontSize: '1.8rem', marginBottom: '2.5rem' }}>{QUESTIONS[currentIdx].q}</h2>

      <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
        {QUESTIONS[currentIdx].a.map((ans, i) => (
          <motion.button
            key={i}
            whileHover={{ scale: 1.02 }}
            whileTap={{ scale: 0.98 }}
            className="btn-primary"
            style={{ background: 'var(--glass)', border: '1px solid var(--glass-border)', color: 'var(--secondary)' }}
            onClick={() => handleAnswer(ans.mood)}
          >
            {ans.text}
          </motion.button>
        ))}
      </div>

      <div style={{ marginTop: '2.5rem', height: '6px', background: 'var(--glass)', borderRadius: '10px', overflow: 'hidden' }}>
        <motion.div
          initial={{ width: 0 }}
          animate={{ width: `${((currentIdx + 1) / QUESTIONS.length) * 100}%` }}
          style={{ height: '100%', background: 'var(--primary)' }}
        />
      </div>
    </motion.div>
  );
};

const DashboardView = ({ user, mood, setMood, setView }) => {
  const [playlist, setPlaylist] = useState([]);
  const [currentSong, setCurrentSong] = useState(null);
  const [isPlaying, setIsPlaying] = useState(false);
  const [favorites, setFavorites] = useState([]);
  const [filter, setFilter] = useState('All');
  const audioRef = useRef(new Audio());

  // Logic moved to Java: Fetch filtered songs from the Java API
  useEffect(() => {
    fetch(`http://localhost:9090/api/songs?mood=${mood}&lang=${filter}`)
      .then(res => res.json())
      .then(data => setPlaylist(data))
      .catch(err => console.error("Error fetching songs from Java API:", err));
  }, [mood, filter]);

  const currentSongIndex = useMemo(() => {
    if (!currentSong) return -1;
    return playlist.findIndex(song => song.id === currentSong.id);
  }, [playlist, currentSong]);

  useEffect(() => {
    const audio = audioRef.current;
    if (currentSong?.url) {
      const songUrl = currentSong.url.startsWith('/') ? currentSong.url : '/' + currentSong.url;
      const currentSrcPath = new URL(audio.src, window.location.origin).pathname;

      if (currentSrcPath !== songUrl) {
        audio.src = currentSong.url;
        audio.load();
        if (isPlaying) audio.play().catch(e => console.log("Playback blocked:", e));
      }
    } else {
      audio.pause();
      audio.src = '';
    }
  }, [currentSong]);

  useEffect(() => {
    if (isPlaying && currentSong?.url) {
      audioRef.current.play().catch(e => console.log("Playback blocked:", e));
    } else {
      audioRef.current.pause();
    }
  }, [isPlaying]);

  useEffect(() => {
    return () => {
      audioRef.current.pause();
      audioRef.current.src = "";
    };
  }, []);

  const toggleFavorite = (id) => {
    setFavorites(prev => prev.includes(id) ? prev.filter(f => f !== id) : [...prev, id]);
  };

  const goToAdjacentSong = (direction) => {
    if (!playlist.length) return;

    const baseIndex = currentSongIndex >= 0 ? currentSongIndex : 0;
    const nextIndex = (baseIndex + direction + playlist.length) % playlist.length;
    setCurrentSong(playlist[nextIndex]);
    setIsPlaying(true);
  };

  return (
    <div style={{ width: '100%', maxWidth: '1200px', display: 'flex', flexDirection: 'column', gap: '3rem' }}>
      <header style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '1rem 0' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
          <div style={{ background: 'var(--primary)', padding: '0.5rem', borderRadius: '10px' }}><Music size={24} /></div>
          <h2 style={{ letterSpacing: '2px', fontSize: '1.2rem', fontWeight: '800' }}>MOODSYNC</h2>
        </div>

        <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', background: 'var(--glass)', padding: '6px 15px', borderRadius: '15px', border: '1px solid var(--glass-border)' }}>
          <button 
            onClick={() => { audioRef.current.pause(); setView('quiz'); }}
            title="Retake Quiz"
            className="icon-btn"
            style={{ color: 'var(--text-dim)', background: 'none', border: 'none', cursor: 'pointer' }}
          >
            <RotateCcw size={18} />
          </button>
          <div style={{ width: '1px', height: '20px', background: 'var(--glass-border)' }} />
          {MOOD_OPTIONS.map(m => (
            <button
              key={m.id}
              onClick={() => { 
                setIsPlaying(false);
                setCurrentSong(null);
                setMood(m.id); 
              }}
              title={`Switch to ${m.id}`}
              className={`mood-btn ${mood === m.id ? 'active' : ''}`}
              style={{ 
                color: mood === m.id ? 'var(--primary)' : 'var(--text-dim)', 
                background: 'none', 
                border: 'none', 
                cursor: 'pointer',
                transition: 'var(--transition)',
                display: 'flex',
                alignItems: 'center'
              }}
            >
              {m.icon}
            </button>
          ))}
        </div>

        <div style={{ display: 'flex', alignItems: 'center', gap: '1.5rem' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.8rem' }}>
            <div style={{ textAlign: 'right' }}>
              <p style={{ fontWeight: '700', fontSize: '0.9rem' }}>{user}</p>
              <p style={{ fontSize: '0.7rem', color: 'var(--primary)', fontWeight: '600' }}>MOOD: {mood.toUpperCase()}</p>
            </div>
            <div style={{ width: '40px', height: '40px', borderRadius: '12px', background: 'linear-gradient(45deg, #8B0000, #333)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}><User size={20} /></div>
          </div>
        </div>
      </header>

      <section style={{ display: 'flex', justifyContent: 'center' }}>
        <motion.div
          initial={{ opacity: 0, scale: 0.9 }}
          animate={{ opacity: 1, scale: 1 }}
          className="premium-card"
          style={{
            width: '100%', maxWidth: '600px',
            background: 'linear-gradient(135deg, #1a1a1a 0%, #0b0b0b 100%)',
            textAlign: 'center', borderTop: '2px solid var(--primary)'
          }}
        >
          <p style={{ color: 'var(--text-dim)', fontSize: '0.8rem', letterSpacing: '4px', marginBottom: '1.5rem' }}>NOW PLAYING</p>
          <h2 style={{ fontSize: '2.5rem', marginBottom: '0.5rem' }}>{currentSong?.title || "Pick a Song"}</h2>
          <p style={{ color: 'var(--text-dim)', fontSize: '1.1rem', marginBottom: '2rem' }}>{currentSong?.artist || "Start the rhythm"}</p>

          <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '3rem', marginBottom: '1.5rem' }}>
            <button
              className="icon-btn"
              style={{ background: 'none', border: 'none', color: 'white' }}
              onClick={() => goToAdjacentSong(-1)}
            >
              <SkipBack size={32} />
            </button>
            <button
              onClick={() => setIsPlaying(!isPlaying)}
              style={{ background: 'var(--primary)', border: 'none', width: '70px', height: '70px', borderRadius: '50%', color: 'white', cursor: 'pointer', display: 'flex', alignItems: 'center', justifyContent: 'center' }}
            >
              {isPlaying ? <Pause size={32} /> : <Play size={32} fill="white" />}
            </button>
            <button
              className="icon-btn"
              style={{ background: 'none', border: 'none', color: 'white' }}
              onClick={() => goToAdjacentSong(1)}
            >
              <SkipForward size={32} />
            </button>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', color: 'var(--primary)', fontSize: '0.8rem', justifyContent: 'center' }}>
            <span style={{ padding: '4px 12px', background: 'rgba(139,0,0,0.2)', borderRadius: '20px', fontWeight: 'bold' }}>{mood}</span>
          </div>
        </motion.div>
      </section>

      <section>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
          <h3 style={{ fontSize: '1.5rem' }}>Your {mood} Playlist</h3>
          <div style={{ display: 'flex', gap: '0.5rem', background: 'var(--glass)', padding: '4px', borderRadius: '12px' }}>
            {LANGUAGE_FILTERS.map(lang => (
              <button
                key={lang}
                onClick={() => setFilter(lang)}
                className={`filter-btn ${filter === lang ? 'active' : ''}`}
              >
                {lang}
              </button>
            ))}
          </div>
        </div>

        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: '1.5rem' }}>
          {playlist.map(song => (
            <motion.div
              key={song.id}
              whileHover={{ scale: 1.03 }}
              className="premium-card red-glow-hover"
              style={{ padding: '1.5rem', cursor: 'pointer', background: 'rgba(255,255,255,0.02)', position: 'relative' }}
              onClick={() => { setCurrentSong(song); setIsPlaying(true); }}
            >
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                <div>
                  <h4 style={{ fontSize: '1.1rem', marginBottom: '0.3rem' }}>{song.title}</h4>
                  <p style={{ color: 'var(--text-dim)', fontSize: '0.9rem' }}>{song.artist}</p>
                </div>
                <button
                  onClick={(e) => { e.stopPropagation(); toggleFavorite(song.id); }}
                  style={{ background: 'none', border: 'none', cursor: 'pointer' }}
                >
                  <Heart size={20} fill={favorites.includes(song.id) ? "var(--primary)" : "none"} color={favorites.includes(song.id) ? "var(--primary)" : "var(--text-dim)"} />
                </button>
              </div>
              <div style={{ marginTop: '1rem', display: 'flex', gap: '0.8rem' }}>
                <span className="mood-tag" style={{ fontSize: '0.7rem' }}>{song.mood}</span>
                <span className="lang-tag">{song.language}</span>
              </div>
            </motion.div>
          ))}
        </div>
      </section>
    </div>
  );
};

function App() {
  const [view, setView] = useState('login');
  const [user, setUser] = useState('');
  const [mood, setMood] = useState(null);

  const handleLogin = (name) => {
    setUser(name);
    setView('quiz');
  };

  const handleQuizComplete = (detectedMood) => {
    setMood(detectedMood);
    setView('dashboard');
  };

  return (
    <div style={{
      minHeight: '100vh',
      display: 'flex',
      justifyContent: 'center',
      alignItems: view === 'dashboard' ? 'flex-start' : 'center',
      padding: '2rem',
      backgroundColor: 'var(--bg-dark)'
    }}>
      {view === 'login' && <LoginView onLogin={handleLogin} />}
      {view === 'quiz' && <QuizView onComplete={handleQuizComplete} />}
      {view === 'dashboard' && <DashboardView user={user} mood={mood} setMood={setMood} setView={setView} />}
    </div>
  );
}

export default App;
