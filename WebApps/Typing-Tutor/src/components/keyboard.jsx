import { useState } from 'react'
import './keyboard.css'

export function Keyboard(){
    const row1Upper = ["~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+"]
    const row1Lower = ["`","1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "="]
    const row2Upper = ["Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "{", "}", "|"]
    const row2Lower = ["q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "[", "]", "\\"]
    const row3Lower = ["a", "s", "d", "f", "g", "h", "j", "k", "l", ";", "'"]
    const row4Lower = ["z", "x", "c", "v", "b", "n", "m", ",", ".", "/"]
    return (
        
        <div className='board'>
            <div className='gap'> </div>
            <span className='row'>
            <main>
            {
                row1Lower.map((letter) =>{
                    return(
                        <span className="key" key={letter}>
                            {letter}
                        </span>
                    )
                })
            }
            </main>
            </span>
            <span className='row'>
            <main>
            {
                row2Lower.map((letter) =>{
                    return(
                        <span className="key" key={letter}>
                            {letter}
                        </span>
                    )
                })
            }
            </main>
            </span>
            <span className='row'>
            <main>
            {
                row3Lower.map((letter) =>{
                    return(
                        <span className="key" key={letter}>
                            {letter}
                        </span>
                    )
                })
            }
            </main>
            </span>
            <span className='row'>
            <span className="key" key="shift1">
                    Shift
                </span>
            <main>
            {
                row3Lower.map((letter) =>{
                        return(
                            <span className="key" key={letter}>
                                {letter}
                            </span>
                        )
                })
            }
            </main>
            <span className="key" key="shift2">
                    Shift
            </span>
            </span>
            <div className="spacebar"> </div>
        </div>
    
    )
}