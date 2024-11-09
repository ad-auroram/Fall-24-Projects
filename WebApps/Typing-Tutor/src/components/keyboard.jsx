import { useState, useEffect } from 'react'
import './keyboard.css'
import {Row} from './keyRow'

export function Keyboard(next){
    const row1Upper = ["~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+"]
    const row1Lower = ["`","1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "="]
    const row2Upper = ["Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "{", "}", "|"]
    const row2Lower = ["q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "[", "]", "\\"]
    const row3Upper = ["A", "S", "D", "F", "G", "H", "J", "K", "L", ":", "\""]
    const row3Lower = ["a", "s", "d", "f", "g", "h", "j", "k", "l", ";", "'"]
    const row4Upper = ["Shift", "Z", "X", "C", "V", "B", "N", "M", "<", ">", "?", "Shift"]
    const row4Lower = ["Shift", "z", "x", "c", "v", "b", "n", "m", ",", ".", "/", "Shift"]

    const [pressedKey, setPressedKey] = useState(null); // Track pressed key
    const [isShiftPressed, setIsShiftPressed] = useState(false); // Track if Shift is pressed
  
    // Handle key down events
    useEffect(() => {
      const handleKeyDown = (event) => {
        event.preventDefault(); // Prevent default browser actions
  
        const keyPressed = event.key;
  
        if (keyPressed === "Shift") {
          setIsShiftPressed(true);
        } else {
          setPressedKey(keyPressed);
        }
      };
  
      const handleKeyUp = (event) => {
        if (event.key === "Shift") {
          setIsShiftPressed(false);
        } else{
          setPressedKey(null)
        }
      };
  

      window.addEventListener('keydown', handleKeyDown);
      window.addEventListener('keyup', handleKeyUp);
  
      return () => {
        window.removeEventListener('keydown', handleKeyDown);
        window.removeEventListener('keyup', handleKeyUp);
      };
    }, []);
  
    const row1 = isShiftPressed ? row1Upper : row1Lower;
    const row2 = isShiftPressed ? row2Upper : row2Lower;
    const row3 = isShiftPressed ? row3Upper : row3Lower;
    const row4 = isShiftPressed ? row4Upper : row4Lower;

    const isSpace = next.next === " ";
    const isPressed = pressedKey === " "
    
    return (
        <div className='board'>
            <div className='gap'></div>
            <Row keys={row1} pressedKey={pressedKey} nextChar = {next} />
            <Row keys={row2} pressedKey={pressedKey} nextChar = {next} />
            <Row keys={row3} pressedKey={pressedKey} nextChar = {next} />
            <Row keys={row4} pressedKey={pressedKey} nextChar = {next} />
            <div className={`key spacebar ${isSpace ? "highlighted" : ""} ${isPressed ? "active" : ""}`}> </div>
            <div className='gap'></div>
        </div>
    )
}