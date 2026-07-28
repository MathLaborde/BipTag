# Walkthrough: Correção dos Marcadores do Mapa

Ajustei as coordenadas e o alinhamento dos marcadores na tela `Maps.kt` para garantir que fiquem 100% visíveis no mapa e com excelente usabilidade.

## O que foi corrigido

### 1. Posição dos Marcadores no Mapa (`Maps.kt`)
- **Alinhamentos Periféricos**: Mudei o cálculo de posição dos marcadores de `Alignment.Center` para alinhamentos estratégicos (`TopCenter`, `CenterEnd`, `BottomStart` e `BottomEnd`).
- **Offsets Calibrados**: Os deslocamentos agora posicionam os marcadores de forma natural sobre o "mapa de ruas" sem que fiquem escondidos atrás do card inferior de detalhes ou da barra de pesquisa superior.

### 2. Destaque do Marcador Selecionado
- **Halo de Seleção**: O marcador ativo do item selecionado no card agora exibe um anel brilhante ao seu redor (`Color.copy(alpha = 0.25f)`) e aumenta de tamanho (de `34.dp` para `42.dp`), deixando claro para o usuário qual pino está selecionado no card de baixo.

## Verificação Realizada
- [x] Os 4 marcadores aparecem visíveis na tela.
- [x] Clicar em um marcador destaca ele visualmente e altera as informações no card de baixo.
- [x] Transição para `LostItemScreen` via o botão do card funciona normalmente.
