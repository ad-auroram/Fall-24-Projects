import { useState } from 'react'
import './App.css'
import {Keyboard} from "./components/keyboard";

function App() {
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
      <div className='space'></div>
      <div>
      <Keyboard/>
      </div>
    </>
  )
}

export default App
