import { useState, useEffect } from 'react';
import './App.css'
import {Keyboard} from "./components/keyboard";


function App() {
  const phrases = [
    "Hello, World!",
    "No place like 127.0.0.1.",
    "The best way to predict the future is to invent it.",
    "The code is mightier than the comment.",
    "Debugging: Being the detective in a criminal movie where you are also the murderer.",
    "To understand recursion, you must first understand recursion.",
    "Code is like humor. When you have to explain it, it is bad.",
    "A computer will do what you tell it to, not what you want it to.",
    "With React, data flows in one direction.",
    "CSS is easy. Just add display flex and hope for the best.",
    "Programming is 10% coding, 90% debugging.",
    "Never underestimate the power of a well-placed console log.",
  ]
  const [targetText, setTargetText] = useState(phrases[Math.floor(Math.random() * phrases.length)]);
  
  const [typedText, setTypedText] = useState("");
  const [nextChar, setNextChar] = useState(targetText[0]);
  const [notTyped, setNotTyped] = useState(targetText.slice(1));
  const [completed, setCompleted] = useState(false);
 
  useEffect(() => {
    const handleKeyDown = (event) => {
      event.preventDefault(); //some keys kept pulling up random things in FireFox for me
  
      const keyPressed = event.key;
  
      if (keyPressed === nextChar && !completed) {
        setTypedText((prev) => prev + keyPressed);
  
        const nextIndex = typedText.length + 1;
        setNextChar(targetText[nextIndex] || "");
  
        setNotTyped(targetText.slice(nextIndex + 1));
  
        if (typedText.length+1 === targetText.length) {
          setCompleted(true);
        }
      }
    };

    window.addEventListener('keydown', handleKeyDown);

    return () => {
      window.removeEventListener('keydown', handleKeyDown);
    };
  }, [nextChar, typedText, targetText, completed]);

  useEffect(() => {
    // When the phrase is completed, switch to the next phrase
    if (completed) {
      let number = Math.floor(Math.random() * phrases.length);
      setNextChar(phrases[number][0]);
      setTargetText(phrases[number]);
      setTypedText("");
      setNotTyped(phrases[number].slice(1));
      setCompleted(false);
    }
  }, [completed, targetText]);

  return (
    <>
      <div className="phrase">
        <span className="typed-text">{typedText}</span>
        <span className="next">{nextChar}</span>
        <span className="remaining-text">{notTyped}</span>
      </div>
      <div className='space'></div>
      <Keyboard next={nextChar}/>
    </>
  )
}

export default App
