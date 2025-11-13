import { createSignal } from 'solid-js'
import solidLogo from '../public/assets/solid.svg'
import citelLogo from '../public/assets/citel.svg'
import clippy from '../public/assets/microsoft-clippy.svg'

import viteLogo from '/vite.svg'
import './App.css'
import axios from "axios";
import i18n from "./i18n.tsx";

interface Prompt {
    role: "user" | "system",
    content: string,
    id: number | null
}

interface LlmResponse {
    parentID: number | null,
    content: string
}
function App() {
    const [prompt, setPrompt] = createSignal({role : "user", content: "", id: null})
    const [prompts, setPrompts] = createSignal<Prompt[]>([])
    const [disableCallLlm, setDisableCallLlm] = createSignal(false)
    let refDiv;
    let refInput;
    let clippyRef;
    let refChat;
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
        p.innerText = prompt().content
        refDiv!.appendChild(p)
        scroll()
        const res: LlmResponse = await (await axios.post("http://localhost:8080/llm", item)).data;
        const p1 = document.createElement('span');
        p1.innerText = res.content
        refDiv!.appendChild(p1)
        scroll()
        setPrompts([...prompts(), {role:"system", content: res.content, id : res.parentID!}])
        setDisableCallLlm(false)
    }
  return (
      <>
          <div className='pulse-container' ref={clippyRef} onClick={() => {
              clippyRef!.style.display = "none"
              refChat!.style.display = "block"
          }}>
              <span className='pulse-button'><img alt={"clippy"} src={clippy} width={80} height={100}/></span>
          </div>
          <div>
              <a href="https://vite.dev" target="_blank">
                  <img src={viteLogo} class="logo" alt="Vite logo"/>
              </a>
              <a href="https://solidjs.com" target="_blank">
                  <img src={solidLogo} class="logo solid" alt="Solid logo"/>
              </a>
          </div>
          <h1>Vite + Solid</h1>

          <div className={"llm-chat-container"} ref={refChat}>
              <div className={"header"}>
                  <span className={"close"} onclick={() => {
                      refChat!.style.display = "none"
                      clippyRef!.style.display = "block"
                  }}>X</span>
                  <img src={citelLogo} alt={"lol"} width={55} height={55} class={"llm-chat-logo"}/>
                  {i18n["FR"].chatBotHeaderMessage}
              </div>
              <div className={"llm-chat"} ref={(el) => {
                  refDiv = el
              }}>

              </div>
              <div style={{display: "flex", "flex-direction": "row", gap: "0"}}>

          <textarea on:input={(e) => {
              setPrompt({...prompt(), content: e.target.value})
          }} style={{width: "100%"}} ref={(el) => refInput = el}/>
                  <button onclick={() => {
                      refInput!.value = "";
                      setPrompts([...prompts(), {
                          role: "user",
                          content: prompt().content,
                          id: prompts().length == 0 ? null : prompts()[prompts().length - 1].id!
                      }])
                      console.log(prompts())
                      setDisableCallLlm(true)
                      return callLlm()
                  }} disabled={disableCallLlm()}>Assistance
                  </button>
              </div>
          </div>

      </>


  )
}

export default App
