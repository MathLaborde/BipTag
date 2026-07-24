# Walkthrough - Implementação da Tela de Mapas

Concluí a implementação da tela de Mapas seguindo fielmente o design proposto e integrando-a ao sistema de navegação do Bip Tag.

## Alterações Realizadas

### Navegação e Infraestrutura
- **[Destination.kt](file:///C:/BipTag/app/src/main/java/br/com/biptag/navigation/Destination.kt)**: Adicionada a nova rota `map_screen`.
- **[BottonBar.kt](file:///C:/BipTag/app/src/main/java/br/com/biptag/components/BottonBar.kt)**: Vinculada a aba "Mapa" à rota correta.
- **[NavigationRoutes.kt](file:///C:/BipTag/app/src/main/java/br/com/biptag/navigation/NavigationRoutes.kt)**: Registrada a `MapsScreen` no NavHost principal.

### Interface do Usuário (UI)
- **[Maps.kt](file:///C:/BipTag/app/src/main/java/br/com/biptag/screens/Maps.kt)**:
    - Criada a nova tela utilizando `Scaffold`.
    - **TopBar**: Integrada com o título "Mapa".
    - **Busca**: Implementada com `BipTagTextField` e ícone de lupa.
    - **Legenda**: Card flutuante indicando "Pessoas próximas" (Azul) e "Itens perdidos" (Vermelho).
    - **Mapa (Simulado)**: Implementado um placeholder utilizando `Canvas` (`drawBehind`) para simular ruas e blocos, garantindo leveza e fidelidade visual sem dependências externas.
    - **Marcadores**: Ícones de localização posicionados estrategicamente para simular dados reais.
    - **FAB**: Botão circular de localização flutuante.
    - **Card de Detalhes**: Card inferior expansivo com informações da "Bicicleta Caloi", incluindo status "Perdida há 2h", distância e o botão de ação `PrimaryButton`.

## Verificação

- **Consistência Visual**: Todas as cores e espaçamentos foram baseados no padrão Material 3 do projeto e na imagem de referência.
- **Reuso de Código**: Utilização dos componentes `TopBar`, `BottomBar`, `PrimaryButton` e `BipTagTextField`.
- **Navegação**: A tela está acessível através da barra inferior.

> [!NOTE]
> A implementação do mapa é visual (placeholder). Para funcionalidades reais de geolocalização, será necessário configurar o Google Maps SDK ou similar no futuro.
