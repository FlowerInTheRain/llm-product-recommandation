export interface Prompt {
    role: "user" | "system",
    content: string,
    id: number | null
}

export interface LlmResponse {
    parentID: number | null,
    content: string
}