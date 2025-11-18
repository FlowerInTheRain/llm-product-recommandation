WITH RECURSIVE some_view AS (
    -- point de départ
    SELECT id, parent_id, prompt, agent_reference
    FROM llm.llm_prompts
    WHERE id = 48

    UNION ALL

    -- on récupère les enfants : ceux pour qui "48" est le parent
    SELECT lp.id, lp.parent_id, lp.prompt, lp.agent_reference
    FROM llm.llm_prompts lp
             JOIN some_view sv ON lp.parent_id = sv.id
)
SELECT *
FROM some_view
ORDER BY id;

SELECT id, parent_id from llm.llm_prompts where id= 48;