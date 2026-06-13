import './ResultSection.css'

function ScoreRing({ score }) {
  const radius = 54
  const circumference = 2 * Math.PI * radius
  const offset = circumference - (score / 100) * circumference

  const color =
    score >= 75 ? '#c8f135' :
    score >= 50 ? '#f0c040' :
    '#ff4d4d'

  return (
    <div className="score-ring-wrap">
      <svg width="140" height="140" viewBox="0 0 140 140" className="score-ring-svg" aria-hidden="true">
        <circle
          cx="70" cy="70" r={radius}
          fill="none"
          stroke="#2a2a2a"
          strokeWidth="8"
        />
        <circle
          cx="70" cy="70" r={radius}
          fill="none"
          stroke={color}
          strokeWidth="8"
          strokeDasharray={circumference}
          strokeDashoffset={offset}
          strokeLinecap="round"
          transform="rotate(-90 70 70)"
          style={{ transition: 'stroke-dashoffset 1s ease' }}
        />
      </svg>
      <div className="score-ring-inner">
        <span className="score-number" style={{ color }}>{score}</span>
        <span className="score-pct">%</span>
      </div>
    </div>
  )
}

function KeywordPill({ word, type }) {
  return (
    <span className={`keyword-pill ${type}`}>
      {type === 'matched' && <span className="pill-dot" aria-hidden="true" />}
      {type === 'missing' && <span className="pill-x" aria-hidden="true">+</span>}
      {word}
    </span>
  )
}

function StatCard({ label, value, sub }) {
  return (
    <div className="stat-card">
      <p className="stat-label">{label}</p>
      <p className="stat-value">{value}</p>
      {sub && <p className="stat-sub">{sub}</p>}
    </div>
  )
}

export default function ResultSection({ result, onReset }) {
  const {
    score = 0,
    matchedKeywords = [],
    missingKeywords = [],
    totalJdKeywords = 0,
  } = result

  const verdict =
    score >= 75 ? { text: 'Strong Match', cls: 'green' } :
    score >= 50 ? { text: 'Partial Match', cls: 'amber' } :
    { text: 'Weak Match', cls: 'red' }

  return (
    <div className="result-section">
      <div className="result-header">
        <div className="result-title-row">
          <h1 className="result-title">Analysis complete.</h1>
          <span className={`verdict-badge ${verdict.cls}`}>{verdict.text}</span>
        </div>
        <button className="reset-btn" onClick={onReset}>
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <polyline points="1 4 1 10 7 10" />
            <path d="M3.51 15a9 9 0 1 0 .49-3.6" />
          </svg>
          Analyze another
        </button>
      </div>

      <div className="score-overview">
        <ScoreRing score={score} />
        <div className="score-stats">
          <StatCard
            label="ATS match score"
            value={`${score}%`}
            sub="cosine similarity"
          />
          <StatCard
            label="Matched keywords"
            value={matchedKeywords.length}
            sub={`out of ${totalJdKeywords}`}
          />
          <StatCard
            label="Missing keywords"
            value={missingKeywords.length}
            sub="to add to resume"
          />
        </div>
      </div>

      <div className="keywords-section">
        <div className="kw-block">
          <div className="kw-header">
            <span className="kw-title">
              <span className="kw-dot matched" aria-hidden="true" />
              Matched keywords
            </span>
            <span className="kw-count matched">{matchedKeywords.length}</span>
          </div>
          <div className="kw-pills">
            {matchedKeywords.length > 0
              ? matchedKeywords.map((k, i) => (
                  <KeywordPill key={i} word={k} type="matched" />
                ))
              : <p className="kw-empty">No keywords matched.</p>
            }
          </div>
        </div>

        <div className="kw-block">
          <div className="kw-header">
            <span className="kw-title">
              <span className="kw-dot missing" aria-hidden="true" />
              Missing keywords
            </span>
            <span className="kw-count missing">{missingKeywords.length}</span>
          </div>
          <p className="kw-tip">
            Add these to your resume to improve your ATS score.
          </p>
          <div className="kw-pills">
            {missingKeywords.length > 0
              ? missingKeywords.map((k, i) => (
                  <KeywordPill key={i} word={k} type="missing" />
                ))
              : <p className="kw-empty">No keywords missing — great job!</p>
            }
          </div>
        </div>
      </div>
    </div>
  )
}
