import { useState } from 'react'
import UploadSection from './components/UploadSection.jsx'
import ResultSection from './components/ResultSection.jsx'
import './App.css'

export default function App() {
  const [result, setResult] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

const handleAnalyze = async (jdFile, resumeFile, jdText, resumeText) => {
  setLoading(true)
  setError(null)
  setResult(null)

  try {
    // Always use FormData — backend handles both file and text fields
    const formData = new FormData()

    if (jdFile) {
      formData.append('jdFile', jdFile)
    } else {
      formData.append('jdText', jdText)
    }

    if (resumeFile) {
      formData.append('resumeFile', resumeFile)
    } else {
      formData.append('resumeText', resumeText)
    }

    const response = await fetch('/api/analyze-mixed', {
      method: 'POST',
      body: formData,
    })

    if (!response.ok) throw new Error(`Server error: ${response.status}`)
    const data = await response.json()
    setResult(data)
  } catch (err) {
    setError(err.message)
  } finally {
    setLoading(false)
  }
}
  const handleReset = () => {
    setResult(null)
    setError(null)
  }

  return (
    <div className="app-layout">
      <header className="app-header">
        <div className="header-inner">
          <div className="logo">
            <span className="logo-bracket">[</span>
            ATS
            <span className="logo-bracket">]</span>
          </div>
          <p className="header-sub">resume × job description matcher</p>
        </div>
      </header>

      <main className="app-main">
        {!result ? (
          <UploadSection
            onAnalyze={handleAnalyze}
            loading={loading}
            error={error}
          />
        ) : (
          <ResultSection result={result} onReset={handleReset} />
        )}
      </main>

      <footer className="app-footer">
        <span className="footer-text">powered by TF-IDF cosine similarity</span>
      </footer>
    </div>
  )
}
