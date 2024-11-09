import { useState } from 'react'
import './App.css'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <div>
      </div>
      <h1>Counting</h1>
      <div className='count'>{count}</div>
      <div className="card">
        <button onClick={() => setCount((count) => count + 1)}>
          Increase +1
        </button>
        <button onClick={() => setCount((count) => count - 1)}>
          Decrease -1
        </button>
      </div>
      <div className="card">
        <button onClick={() => setCount((count) => count + 5)}>
          Increase +5
        </button>
        <button onClick={() => setCount((count) => count - 5)}>
          Decrease -5
        </button>
      </div>
      <div className="card">
        <button onClick={() => setCount((count) => count * 2)}>
          Multiply x2
        </button>
        <button onClick={() => setCount((count) => count / 2)}>
          Divide /2
        </button>
      </div>
    </>
  )
}

export default App
