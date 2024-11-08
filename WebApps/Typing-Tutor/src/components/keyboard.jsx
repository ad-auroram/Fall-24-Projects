import { useState } from 'react'
import './keyboard.css'
import {Row} from './keyRow'

export function Keyboard(){
    const row1Upper = ["~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+"]
    const row1Lower = ["`","1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "="]
    const row2Upper = ["Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "{", "}", "|"]
    const row2Lower = ["q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "[", "]", "\\"]
    const row3Upper = ["A", "S", "D", "F", "G", "H", "J", "K", "L", ":", "\""]
    const row3Lower = ["a", "s", "d", "f", "g", "h", "j", "k", "l", ";", "'"]
    const row4Upper = ["Shift", "Z", "X", "C", "V", "B", "N", "M", "<", ">", "?", "Shift"]
    const row4Lower = ["Shift", "z", "x", "c", "v", "b", "n", "m", ",", ".", "/", "Shift"]
    return (
        
        <div className='board'>
            <div className='gap'> </div>
            <Row keys={row1Lower} />
            <Row keys={row2Lower} />
            <Row keys={row3Lower} />
            <Row keys={row4Lower} />
            <div className="key spacebar"> </div>
            <div className='gap'> </div>
        </div>
    
    )
}