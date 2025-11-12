import { createSignal } from 'solid-js'
import solidLogo from '../public/assets/solid.svg'
import citelLogo from '../public/assets/citel.svg'

import viteLogo from '/vite.svg'
import './App.css'
import axios from "axios";

function App() {
    const [prompt, setPrompt] = createSignal("")
    let refDiv;
    const callLlm = async () => {
      const item = {
          content: prompt()
      }
        const p = document.createElement('span');
        p.textContent = prompt()
        refDiv!.appendChild(p)
      const res = await axios.post("http://localhost:8080/llm", item);
        const p1 = document.createElement('span');

        p1.textContent = res.data
        refDiv!.appendChild(p1)
    }
  return (
    <>
      <div>
        <a href="https://vite.dev" target="_blank">
          <img src={viteLogo} class="logo" alt="Vite logo" />
        </a>
        <a href="https://solidjs.com" target="_blank">
          <img src={solidLogo} class="logo solid" alt="Solid logo" />
        </a>
      </div>
      <h1>Vite + Solid</h1>
      <div style={{display:"flex", "flex-direction":"column", gap: "15px"}}>

          <textarea on:input={(e) => {
              setPrompt(e.target.value)
          }} style={{width:"100%"}}/>
          <button onclick={() =>callLlm()}>Call llm</button>
      </div>
        <div class={"llm-chat"} ref={(el) => {
            refDiv = el
        }}>
            <div class={"header"}>
                <img src={citelLogo} alt={"lol"} width={45} height={45} class={"llm-chat-logo"}/>
                2mande à Lia
            </div>

        </div>
      <p class="read-the-docs">
        Click on the Vite and Solid logos to learn more
      </p>
    </>
  )
}

export default App
