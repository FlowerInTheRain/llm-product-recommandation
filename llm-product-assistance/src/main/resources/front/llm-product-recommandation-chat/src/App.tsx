import { createSignal } from 'solid-js'
import solidLogo from '../public/assets/solid.svg'
import citelLogo from '../public/assets/citel.svg'
import viteLogo from '/vite.svg'
import './App.css'
import axios from "axios";
import i18n from "./i18n.tsx";

interface Prompt {
    role: "user" | "system",
    content: string
}
function App() {
    const [prompt, setPrompt] = createSignal("")
    const [prompts, setPrompts] = createSignal<Prompt[]>([])
    const [disableCallLlm, setDisableCallLlm] = createSignal(false)
    let refDiv;
    let refInput;

    const scroll = () => {
        refDiv!.scrollTo({
            top: refDiv!.scrollHeight,
            behavior: "smooth",
        }) ;
    }
    const callLlm = async () => {
      const item = {
          content: prompts()
      }
        const p = document.createElement('span');
        p.innerText = prompt()
        refDiv!.appendChild(p)
        scroll()
        const res = await axios.post("http://localhost:8080/llm", item);
        const p1 = document.createElement('span');
        p1.innerText = res.data
        refDiv!.appendChild(p1)
        scroll()
        setPrompts([...prompts(), {role:"system", content: res.data}])
        setDisableCallLlm(false)
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

        <div class={"llm-chat-container"} >
            <div class={"header"}>
                <img src={citelLogo} alt={"lol"} width={55} height={55} class={"llm-chat-logo"}/>
                {i18n["FR"].chatBotHeaderMessage}
            </div>
            <div class={"llm-chat"} ref={(el) => {
                refDiv = el
            }}>

            </div>
            <div style={{display:"flex", "flex-direction":"row", gap: "0"}}>

          <textarea on:input={(e) => {
              setPrompt(e.target.value)
          }} style={{width:"100%"}} ref={(el) => refInput = el}/>
                <button onclick={() => {
                    refInput!.value = "";
                    setPrompts([...prompts(),{role: "user", content: prompt()}])
                    setDisableCallLlm(true)
                    return callLlm()
                }} disabled={disableCallLlm()}>Assistance</button>
            </div>
        </div>

    </>
  )
}

export default App
