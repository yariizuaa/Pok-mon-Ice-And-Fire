# Pokémon Ice & Fire

Projeto de referência para o trabalho **Pokémon — Captura e Batalha** (1º bimestre,
Opção C). Sistema de captura e batalha de criaturas por turnos, com interface
gráfica em Java Swing.

## Sobre as criaturas

As criaturas jogáveis (**Charmander**, **Squirtle**, **Bulbasaur**) e os sprites
usados são da franquia Pokémon (Nintendo/Game Freak), com uso autorizado pela
professora da disciplina para fins deste trabalho acadêmico. A mecânica (tipos
elementares, captura, equipe, batalha por turnos) é original.

## Estrutura do projeto

```
src/PokemonIceFire/
├── Main.java                  → ponto de entrada
├── modelo/
│   ├── TipoElemental.java     → enum com a lógica de vantagem de tipo
│   ├── Pokemon.java           → classe abstrata base (estado + comportamento + nível/XP)
│   ├── Experiencia.java       → calcula a XP ganha ao vencer uma batalha
│   ├── Charmander.java        → tipo Fogo (habilidade: Labareda)
│   ├── Squirtle.java          → tipo Água (habilidade: Maré Cheia)
│   ├── Bulbassaur.java        → tipo Planta (habilidade: Sugar Seiva)
│   └── FabricaPokemons.java   → cria os pokémons iniciais e encontros selvagens (define o nível)
├── treinador/
│   ├── Treinador.java         → equipe do jogador (até 6 pokémons)
│   └── Pokedex.java           → registro de espécies já vistas/capturadas
├── batalha/
│   ├── Batalha.java           → laço de turnos entre dois pokémons
│   └── Captura.java           → cálculo da chance de captura
├── item/
│   ├── TipoPocao.java         → enum das poções (cura, peso no sorteio)
│   ├── TipoRevive.java        → enum dos reviveres (% de vida ao acordar, peso no sorteio)
│   └── Loot.java              → sorteia os itens (poção e revive) ganhos ao vencer uma batalha
└── view/                      → toda a interface gráfica (Swing)
    ├── CriaturaSprite.java    → desenha o pokémon a partir da imagem .png do tipo
    ├── sprites/                → imagens .png usadas pelo CriaturaSprite
    │   ├── charmander.png
    │   ├── squirtle.png
    │   └── bulbassaur.png
    ├── BarraVida.java         → componente de barra de vida
    ├── PainelArena.java       → cenário de batalha
    ├── CartaoPokemon.java     → cartão usado na tela de escolha inicial
    ├── PainelMenu.java        → tela inicial
    ├── PainelEscolha.java     → escolha do pokémon inicial
    ├── PainelBatalha.java     → tela principal (explorar, atacar, capturar, fugir)
    ├── PainelEquipe.java      → equipe do treinador + pokédex
    ├── PainelItens.java       → aba Itens: inventário de poções e cura da equipe
    └── JanelaPrincipal.java   → janela principal (CardLayout entre as telas)

test/PokemonIceFire/           → testes unitários (JUnit 5), mesma estrutura de pacotes
```

## Como compilar e rodar

Requer JDK 11 ou superior (testado com JDK 21).

```bash
# a partir da raiz do projeto
javac -d out $(find src -name "*.java")
cp -r src/PokemonIceFire/view/sprites out/PokemonIceFire/view/
cp -r src/PokemonIceFire/view/backgrounds out/PokemonIceFire/view/
java -cp out PokemonIceFire.Main
```

> Os passos `cp -r ... out/PokemonIceFire/view/` copiam as imagens dos
> pokémons e o fundo da tela inicial para dentro da pasta compilada — sem
> eles, o `javac` compila só os `.java` e as imagens não são encontradas em
> tempo de execução.

## Sprites dos pokémons (imagens .png)

Cada pokémon é desenhado a partir de um arquivo de imagem, um por **tipo**
elemental (não por indivíduo — todo Charmander selvagem usa `charmander.png`,
por exemplo):

```
src/PokemonIceFire/view/sprites/
├── charmander.png  (Fogo)
├── squirtle.png    (Água)
└── bulbassaur.png  (Planta)
```

- Se algum arquivo não existir, o jogo não quebra: `CriaturaSprite.java`
  volta automaticamente para um desenho vetorial simples e avisa no console
  qual arquivo está faltando.
- Ao tomar dano/desmaiar, a imagem é convertida para tons de cinza
  automaticamente (não precisa de uma segunda versão da imagem para isso).
- Depois de trocar as imagens, lembre de repetir o `cp -r` do passo de
  compilação (ou recompilar do zero) para que a versão nova seja copiada.

## Imagem de fundo da tela inicial

A tela de título (`PainelMenu.java`) usa a imagem `src/PokemonIceFire/view/backgrounds/menu.jpg`
como plano de fundo, redimensionada para preencher a janela inteira sem
distorcer (modo "cover" — corta as bordas quando a proporção não bate) e com
uma camada escura semitransparente por cima para manter o título e os
botões legíveis.

- Para trocar a imagem, basta substituir esse arquivo por outro `.jpg`
  (mesmo nome) e repetir o `cp -r` do passo de compilação.
- Se o arquivo não existir, a tela volta automaticamente para o gradiente
  azul + marca d'água de pokébolas original, e avisa no console.

## Como rodar os testes unitários

Os testes usam JUnit 5 (Jupiter). Se você não usa Maven/Gradle, o jeito mais
simples é baixar o **JUnit Console Standalone** e rodar:

```bash
javac -d out $(find src -name "*.java")
javac -cp out:junit-platform-console-standalone.jar -d out-test $(find test -name "*.java")
java -jar junit-platform-console-standalone.jar -cp out:out-test --scan-classpath
```

Se preferir Maven/Gradle, basta apontar `src` como source root, `test` como
test root, e adicionar a dependência `org.junit.jupiter:junit-jupiter:5.10.0`
(ou versão mais recente) no escopo de teste.

## Mecânica de itens (poções e reviveres)

Ao vencer uma batalha (derrotar o pokémon selvagem), há **75% de chance** de
encontrar uma poção, sorteada entre três tipos (poções mais fortes são mais
raras — pesos em `TipoPocao`):

| Poção          | Cura   | Peso no sorteio |
|----------------|--------|------------------|
| Poção Pequena  | 20 HP  | 50               |
| Poção Média    | 50 HP  | 30               |
| Poção Grande   | 100 HP | 15               |

Independentemente da poção, há também **35% de chance** de encontrar um
revive, sorteado entre dois tipos (pesos em `TipoRevive`):

| Revive      | Vida ao acordar        | Peso no sorteio |
|-------------|-------------------------|------------------|
| Revive      | 50% da vida máxima      | 20               |
| Revive Max  | 100% da vida máxima     | 8                |

O revive é o único item capaz de acordar um pokémon **desmaiado** (vida
zerada) — poções normais só curam criaturas que ainda estão vivas. Os dois
sorteios acontecem de forma independente, então uma mesma vitória pode
render poção, revive, os dois, ou nenhum.

Os itens vão para o inventário do `Treinador` (`Treinador.adicionarItem` /
`adicionarItemRevive`) e aparecem na aba **Itens**, acessível pelo botão no
cabeçalho da tela de Batalha (ao lado de "Equipe / Bestiário") ou pelo botão
"Ver itens" na aba Equipe. Nessa aba, o botão "Usar" em cada poção pede qual
criatura da equipe deve ser curada (só oferece as que estão vivas e não
estão com vida cheia); o botão "Usar" em cada revive pede qual criatura
desmaiada deve acordar (só oferece as que estão desmaiadas). Ambos aplicam o
efeito imediatamente — inclusive durante uma batalha em andamento.

A lógica de sorteio fica isolada em `PokemonIceFire.item.Loot`, e a lógica de
inventário (guardar, consultar e consumir itens) fica em `Treinador`, então
adicionar um novo tipo de item não exige mexer na tela de batalha.

## Mecânica de nível e experiência (XP)

O nível de cada criatura aparece ao lado do nome (na barra de vida da
batalha e nos cartões da aba Equipe), no formato "Nome  Nv.X":

- O pokémon **inicial**, escolhido pelo treinador, sempre começa no
  **nível padrão (10)** — `FabricaPokemons.NIVEL_PADRAO`.
- Todo pokémon **selvagem** encontrado ao explorar recebe um nível
  **aleatório entre o nível padrão e o nível padrão + 5** (ou seja, 10 a 15),
  sorteado em `FabricaPokemons.selvagemAleatorio()`. Se for capturado, entra
  na equipe já com esse nível.
- Ao **vencer uma batalha** (derrotar o pokémon selvagem), o pokémon do
  jogador que participou da luta ganha XP com base no nível do inimigo
  derrotado (inimigos mais fortes rendem mais XP — ver
  `Experiencia.calcularXpDeVitoria`).
- Quando o XP acumulado atinge o necessário para o nível atual
  (`Pokemon.getXpParaProximoNivel()`, igual a `nível atual × 12` — reduzido
  de 30 para 12, ou seja, sobe de nível bem mais rápido), a criatura **sobe
  de nível automaticamente** — podendo subir mais de um nível de uma vez se
  o XP ganho for suficiente — e ganha um pequeno reforço de vida máxima,
  ataque e defesa. O registro de batalha avisa tanto o XP ganho quanto uma
  eventual subida de nível, e uma janela também aparece em tela avisando
  que o pokémon está evoluindo.

Toda essa lógica fica isolada em `Pokemon` (nível/XP) e `Experiencia`
(cálculo de XP de vitória), então ajustar o ritmo de progressão não exige
mexer na tela de batalha.

## Derrota completa e reinício da jornada

Se **todos** os pokémon da equipe do treinador ficarem desmaiados ao mesmo
tempo (`Treinador.getAtiva()` retorna `null`), a tela de Batalha esconde os
botões normais (Explorar/Atacar/Capturar/Fugir) e mostra apenas o botão
**"Reiniciar Jornada"**. Ao clicar nele, o treinador e o bestiário são
zerados (`JanelaPrincipal.reiniciarJornada()`) e o jogo volta para a tela de
escolha do pokémon inicial, como se estivesse começando do zero.

mexer na tela de batalha.

## Mecânica de tipos

Fogo vence Planta · Planta vence Água · Água vence Fogo (vantagem: 1.5x dano,
desvantagem: 0.67x dano) — a lógica fica centralizada em
`TipoElemental.vantagemSobre(...)`, então adicionar um novo tipo não exige
reescrever o código de batalha.

## Habilidades especiais (polimorfismo)

Cada subclasse de `Pokemon` sobrescreve `bonusEspecial(...)` e, quando faz
sentido, `efeitoPosAtaque(...)`:

- **Charmander** (Fogo): +30% de dano contra alvos com vida abaixo de 30%
- **Squirtle** (Água): +15% de dano enquanto a própria vida estiver acima de 50%
- **Bulbasaur** (Planta): recupera 20% do dano causado como vida a cada ataque
