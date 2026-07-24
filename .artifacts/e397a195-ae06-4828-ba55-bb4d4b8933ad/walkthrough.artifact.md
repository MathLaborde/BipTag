# Walkthrough - Implementação da Tela de Item Perdido

Concluí a implementação da tela de detalhes de item perdido (`LostItem.kt`) e sua integração com a tela de mapas.

## Alterações Realizadas

### Navegação
- **[Destination.kt](file:///C:/BipTag/app/src/main/java/br/com/biptag/navigation/Destination.kt)**: Adicionada a rota `lost_item_screen`.
- **[NavigationRoutes.kt](file:///C:/BipTag/app/src/main/java/br/com/biptag/navigation/NavigationRoutes.kt)**: Registrada a `LostItemScreen` no NavHost.
- **[Maps.kt](file:///C:/BipTag/app/src/main/java/br/com/biptag/screens/Maps.kt)**: O botão "Ver item perdido" agora navega corretamente para a tela de detalhes.

### Interface do Usuário (UI)
- **[LostItem.kt](file:///C:/BipTag/app/src/main/java/br/com/biptag/screens/LostItem.kt)**:
    - **TopBar**: Título "Item perdido" com navegação de volta funcional.
    - **Header**: Seção de imagem com placeholder e badge de status "Perdido há 3h".
    - **Informações do Reportador**: Card detalhado com nome, tempo de reporte e distância.
    - **Localização**: Mini mapa placeholder com tag de endereço flutuante ("Av. Paulista, ~1000").
    - **Segurança**: Card informativo sobre o processo de entrega via pontos parceiros.
    - **Ação**: Botão fixo no rodapé "Estou com este item".

## Verificação
- **Transição de Telas**: Clique no botão do card no mapa e verifique se a tela abre corretamente.
- **Fidelidade Visual**: O layout segue exatamente o design fornecido, incluindo cores, bordas arredondadas e ícones.
- **Reuso**: Foram utilizados os componentes padrão `TopBar` e `PrimaryButton`.
