import './keyboard.css'
export function Row({ keys, pressedKey }) {
    let count = 1;
    return (
        <div className="row">
          {keys.map((letter) => {
            const isPressed = letter === pressedKey; // Check if the key is pressed
            let currentCount = count;
            count++;
    
            return (
              <span
                key={letter + currentCount}
                className={`key ${isPressed ? "active" : ""}`} // Add "highlighted" class if key is pressed
              >
                {letter}
              </span>
            );
          })}
        </div>
      );
    }