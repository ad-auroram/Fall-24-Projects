import './keyboard.css'
export function Row({ keys, pressedKey, nextChar }) {
    let count = 1;
    return (
        <div className="row">
          {keys.map((letter) => {
            const isPressed = letter === pressedKey;
            const isShift = letter ==="Shift";
            const next = nextChar.next;
            const isNext = String(letter).trim().toLowerCase() === String(next).trim().toLowerCase();
            let currentCount = count;
            count++;
    
            return (
              <span
                key={letter + currentCount}
                className={`key ${isPressed ? "active" : ""} ${isShift ? "shift" : ""} ${isNext ? "highlighted" : ""}`}
              >
                {letter}
              </span>
            );
          })}
        </div>
      );
    }