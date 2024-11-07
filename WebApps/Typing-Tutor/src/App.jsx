import { useState } from 'react'
import './App.css'

function App() {
  const [count, setCount] = useState(0)
  const typedText = "Woah th"
  const nextChar = "i"
  const notTyped = "s is crazy."
  return (
    <>
      <div className="phrase">
        <span className="typed-text">{typedText}</span>
        <span className="next">{nextChar}</span>
        <span className="remaining-text">{notTyped}</span>
      </div>
      <div className="keyboard">
        
      </div>
    </>
  )
}

export default App
