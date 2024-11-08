import './keyboard.css'
export function Row({ keys }) {
    let count = 1;
    return (
        <div className="row">
            {keys.map((letter) => {
                if (letter === "Shift") {
                    let currentCount = count;
                    count++;
                    return (
                        <span className="key shift" key={letter + currentCount}>
                            {letter}
                        </span>
                    );
                } else {
                    return (
                        <span className="key" key={letter}>
                            {letter}
                        </span>
                    );
                }
            })}
        </div>
    );
}