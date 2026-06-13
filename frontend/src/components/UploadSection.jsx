import { useState, useRef } from 'react'
import './UploadSection.css'

function DropZone({ label, sublabel, file, onFile, accept }) {
  const inputRef = useRef(null)
  const [dragging, setDragging] = useState(false)

  const handleDrop = (e) => {
    e.preventDefault()
    setDragging(false)
    const dropped = e.dataTransfer.files[0]
    if (dropped) onFile(dropped)
  }

  return (
    <div
      className={`drop-zone ${dragging ? 'dragging' : ''} ${file ? 'has-file' : ''}`}
      onClick={() => inputRef.current.click()}
      onDragOver={(e) => { e.preventDefault(); setDragging(true) }}
      onDragLeave={() => setDragging(false)}
      onDrop={handleDrop}
    >
      <input
        ref={inputRef}
        type="file"
        accept={accept}
        style={{ display: 'none' }}
        onChange={(e) => { if (e.target.files[0]) onFile(e.target.files[0]) }}
      />
      <div className="dz-icon">
        {file ? (
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5">
            <polyline points="20 6 9 17 4 12" />
          </svg>
        ) : (
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5">
            <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
            <polyline points="17 8 12 3 7 8" />
            <line x1="12" y1="3" x2="12" y2="15" />
          </svg>
        )}
      </div>
      <p className="dz-label">{label}</p>
      <p className="dz-sublabel">{file ? file.name : sublabel}</p>
      {file && (
        <button className="dz-clear" onClick={(e) => { e.stopPropagation(); onFile(null) }}>✕</button>
      )}
    </div>
  )
}

function PasteZone({ label, value, onChange, placeholder }) {
  return (
    <div className="paste-zone">
      <textarea
        className={`paste-textarea ${value ? 'has-text' : ''}`}
        value={value}
        onChange={(e) => onChange(e.target.value)}
        placeholder={placeholder}
        rows={8}
      />
      {value && (
        <button className="paste-clear" onClick={() => onChange('')}>✕ Clear</button>
      )}
    </div>
  )
}

// Single panel — toggle between file and paste independently
function InputPanel({ slotTag, label, file, onFile, text, onText, accept }) {
  const [mode, setMode] = useState('file')

  return (
    <div className="input-panel">
      <div className="panel-header">
        <span className="slot-tag">{slotTag}</span>
        <div className="mini-toggle">
          <button
            className={`mini-btn ${mode === 'file' ? 'active' : ''}`}
            onClick={() => setMode('file')}
          >
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
              <polyline points="17 8 12 3 7 8" />
              <line x1="12" y1="3" x2="12" y2="15" />
            </svg>
            File
          </button>
          <button
            className={`mini-btn ${mode === 'paste' ? 'active' : ''}`}
            onClick={() => setMode('paste')}
          >
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <path d="M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2" />
              <rect x="8" y="2" width="8" height="4" rx="1" ry="1" />
            </svg>
            Paste
          </button>
        </div>
      </div>

      {mode === 'file' ? (
        <DropZone
          label={label}
          sublabel="PDF or TXT"
          file={file}
          onFile={onFile}
          accept={accept}
        />
      ) : (
        <PasteZone
          label={label}
          value={text}
          onChange={onText}
          placeholder={`Paste ${label.toLowerCase()} here...`}
        />
      )}
    </div>
  )
}

export default function UploadSection({ onAnalyze, loading, error }) {
  const [jdFile, setJdFile] = useState(null)
  const [resumeFile, setResumeFile] = useState(null)
  const [jdText, setJdText] = useState('')
  const [resumeText, setResumeText] = useState('')

  const jdReady = jdFile || jdText.trim()
  const resumeReady = resumeFile || resumeText.trim()
  const canAnalyze = jdReady && resumeReady && !loading

  const handleSubmit = () => {
    if (!canAnalyze) return
    onAnalyze(jdFile, resumeFile, jdText, resumeText)
  }

  return (
    <div className="upload-section">
      <div className="upload-heading">
        <h1 className="upload-title">Drop your files.</h1>
        <p className="upload-desc">
          Upload or paste your job description and resume — mix and match however you like.
        </p>
      </div>

      <div className="drop-grid">
        <InputPanel
          slotTag="01 / job description"
          label="Job Description"
          file={jdFile}
          onFile={setJdFile}
          text={jdText}
          onText={setJdText}
          accept=".pdf,.txt"
        />
        <div className="drop-connector" aria-hidden="true">×</div>
        <InputPanel
          slotTag="02 / resume"
          label="Resume"
          file={resumeFile}
          onFile={setResumeFile}
          text={resumeText}
          onText={setResumeText}
          accept=".pdf,.txt"
        />
      </div>

      {error && (
        <div className="error-bar" role="alert">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <circle cx="12" cy="12" r="10" />
            <line x1="12" y1="8" x2="12" y2="12" />
            <line x1="12" y1="16" x2="12.01" y2="16" />
          </svg>
          {error}
        </div>
      )}

      <button
        className={`analyze-btn ${loading ? 'loading' : ''}`}
        onClick={handleSubmit}
        disabled={!canAnalyze}
      >
        {loading ? (
          <><span className="spinner" aria-hidden="true" />Analyzing…</>
        ) : (
          <>Run Analysis
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <line x1="5" y1="12" x2="19" y2="12" />
              <polyline points="12 5 19 12 12 19" />
            </svg>
          </>
        )}
      </button>

      <p className="upload-hint">Files are processed locally — nothing stored or sent to third parties.</p>
    </div>
  )
}