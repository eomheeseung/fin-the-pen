import logo from './logo.svg'; // eslint-disable-line no-unused-vars
import './App.css';
import React, {useState, useEffect} from 'react';

function App() {
    const [message, setMessage]=useState([]);
    useEffect(()=>{
        fetch("/hello")
            .then((res)=>{
                return res.json();
            })
            .then((data)=>{
                setMessage(data);
            });
    },[]);
    return (
        <div className="App">
            <header className="App-header">
                <ul>
                    {message.map((v,idx)=><li key={`${idx}-${v}`}>{v}</li>)}
                </ul>
            </header>
        </div>
    );
}

export default App;