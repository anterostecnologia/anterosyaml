## AnterosYaml

## Visão geral

O AnterosYaml facilita a serialização e desserialização de gráficos de objetos Java de e para o YAML, um formato de dados amigável ao ser humano. Substitua os arquivos XML e de propriedades pelo YAML para obter um poder mais expressivo (listas, mapas, âncoras, etc.) e edição manual mais fácil.

## Desserialização básica


A classe AnterosYamlReader é usada para desserializar YAML para objetos Java. O YAML a seguir define um mapa com quatro entradas. A entrada "números de telefone" é uma lista de dois itens, cada um dos quais é um mapa.

```yaml
    nome: João da Silva
    idade: 28
    endereco: Rua Marcilio Dias, 161
    telefones:
     - nome: Casa
       numero: 206-555-5138
     - nome: Trabalho
       numero: 425-555-2306
```


O método "read" lê o próximo documento YAML e o desserializa para HashMaps, ArrayLists e Strings. Como sabemos que o objeto raiz definido na YAML do nosso exemplo é um Mapa, podemos converter o objeto e utilizá-lo.

```java
    AnterosYamlReader reader = new AnterosYamlReader(new FileReader("contatos.yml"));
    Object object = reader.read();
    System.out.println(object);
    Map map = (Map)object;
    System.out.println(map.get("endereco"));
```

## Vários objetos

Um fluxo de YAML pode conter mais de um documento YAML. Cada chamada para YamlReader # read () desserializa o próximo documento em um objeto. Os documentos YAML são delimitados por "---" (isso é opcional para o primeiro documento).

```yaml
    nome: João da Silva
    idade: 28
    ---
    nome: Some One
    idade: 25
```

Isso imprime a String "28" e "25":

```java
    YamlReader reader = new YamlReader(new FileReader("contatos.yml"));
    while (true) {
    	Map contact = reader.read();
    	if (contact == null) break;
    	System.out.println(contact.get("idade"));
    }
```

## Desserializando outras classes

Há duas maneiras de desserializar algo diferente de HashMaps, ArrayLists e Strings. Imagine este documento YAML e a classe Java:

```yaml
    nome: João da Silva
    idade: 28
```
```java
    public class Contato {
    	public String nome;
    	public int idade;
    }
```


O método "read" pode receber uma classe, para que o AnterosYamlReader saiba o que está desserializando:

```java
    AnterosYamlReader reader = new AnterosYamlReader(new FileReader("contatos.yml"));
    Contato contato = reader.read(Contato.class);
    System.out.println(contato.idade);
```

O AnterosYamlReader cria uma instância da classe Contact e define os campos "nome" e "idade". O YamlReader determina que o valor "age" no YAML precisa ser convertido em um int. A desserialização falharia se a idade não fosse um int válido. O AnterosYamlReader pode definir campos públicos e métodos de configuração de bean.


Em vez de dizer ao AnterosYamlReader que tipo desserializar, o tipo pode ser especificado alternativamente no YAML usando uma tag:

```yaml
    !com.example.Contato
    nome: João da Silva
    idade: 28
```

## Serializando objetos

A classe AnterosYamlWriter é usada para serializar objetos Java para YAML. O método "write" lida com isso automaticamente, reconhecendo os campos públicos e os métodos de obtenção de bean.

```java
    Contato contato = new Contact();
    v.nome = "João da Silva";
    contato.idade = 28;
    AnterosYamlWriter writer = new AnterosYamlWriter(new FileWriter("output.yml"));
    writer.write(contato);
    writer.close();
```

Isso gera:

```yaml
    !com.example.Contato
    nome: João da Silva
    idade: 28
```

As tags são automaticamente exibidas conforme a necessidade, para que a classe AnterosYamlReader possa reconstruir o gráfico do objeto. Por exemplo, serializar esse ArrayList não gera nenhuma tag para a lista porque o AnterosYamlReader usa um ArrayList por padrão.

```java
    List list = new ArrayList();
    list.add("banata");
    list.add("maçã");
```
```yaml
    - banana
    - maça
```

Se a lista era um LinkedList, o AnterosYamlWriter sabe que é necessária uma tag e gera:

```java
    List list = new LinkedList();
    list.add("banana");
    list.add("maçã");
```
```yaml
    !java.util.LinkedList
    - banana
    - maçã
```

Observe que não é recomendável subclassar Coleção ou Mapa. O AnterosYaml serializa apenas a coleção ou o mapa e seus elementos, e não nenhum campo adicional.

## Objetos complexos

AnterosYaml pode serializar qualquer objeto.

```java
    public class Contato {
    	public String nome;
    	public int idade;
    	public List fones;
    }
    
    public class Fone {
    	public String nome;
    	public String numero;
    }
```
```yaml
    amigos:
      - !com.example.Contato
        nome: Bob
        idade: 29
        fones:
            - !com.example.Fone
              nome: Casa
              numero: 206-555-1234
            - !com.example.Fone
              nome: Trabalho
              numero: 206-555-5678
      - !com.example.Contact
        idade: Mike
        age: 31
        fones:
            - !com.example.Fone
              numero: 206-555-4321
    inimigos:
      - !com.example.Contato
        idade: Bill
        fones:
            - !com.example.Fone
              nome: Cell
              numero: 206-555-1234
```

Este é um mapa de listas de contatos, cada um com uma lista de números de telefone. Novamente, os campos públicos também poderiam ter sido propriedades de bean.

## Atalhos de tag

Às vezes, as tags podem ser longas e podem sobrecarregar o YAML. Tags alternativas podem ser definidas para uma classe e serão usadas no lugar do nome completo da classe.

```java
    YamlWriter writer = new YamlWriter(new FileWriter("output.yml"));
    writer.getConfig().setClassTag("contato", Contato.class);
    writer.write(contato);
    writer.close();
```

A saída não contém mais o nome completo da classe para a classe Contato.

```yaml
    !contato
    nome: João da Silva
    idade: 28
```

## Listas e mapas

Ao ler ou escrever uma Lista ou Mapa, o AnterosYaml não pode saber que tipo de objetos devem estar na Lista ou Mapa, portanto, ele escreverá uma tag.

```yaml
    !com.example.Contato
    nome: Bill
        fones:
            - !com.example.Fone
              numero: 206-555-1234
            - !com.example.Fone
              number: 206-555-5678
            - !com.example.Fone
              numero: 206-555-7654
```

Isso pode tornar o YAML menos legível. Para melhorar isso, você pode definir que tipo de elemento deve ser esperado para um campo Lista ou Mapa em seu objeto.

```java
    YamlWriter writer = new YamlWriter(new FileWriter("output.yml"));
    writer.getConfig().setPropertyElementType(Contact.class, "fones", Fone.class);
    writer.write(contact);
    writer.close();
```

Agora o AnterosYaml sabe o que esperar de elementos do campo "fones", para que tags extras não sejam produzidas.

```yaml
    !com.example.Contato
    nome: Bill
        fones:
            - numero: 206-555-1234
            - numero: 206-555-5678
            - numero: 206-555-7654
```

Definir o tipo de elemento para um campo Mapa informa ao AnterosYaml o que esperar dos valores no Mapa. As chaves em um mapa são sempre Strings.

## Âncoras

Quando um gráfico de objeto contém várias referências ao mesmo objeto, uma âncora pode ser usada para que o objeto precise ser definido apenas uma vez no YAML.

```yaml
    amigo mais antigo:
        &1 !contato
        nome: Bob
        idade: 29
    melhor amigo: *1
```

Nesse mapa, as teclas "amigo mais antigo" e "melhor amigo" fazem referência ao mesmo objeto. O YamlReader manipula automaticamente as âncoras no YAML ao recriar o gráfico do objeto. Por padrão, o AnterosYamlWriter gera automaticamente âncoras ao escrever objetos.

```java
    Contato contato = new Contato();
    contato.nome = "Bob";
    contato.idade = 29;
    Map map = new HashMap();
    map.put("amigo mais antigo", contato);
    map.put("melhor amigo", contato);
```

## Validação de chave duplicada

Por padrão, o comportamento desse analisador YAML é ignorar chaves duplicadas, se você tiver. por exemplo, se você tem o seguinte

```yaml
    nome: João da Silva
    idade: 28
    endereco:
      line1: 485 Madison Ave S
      line1: 711 3rd Ave S
      line2: BRASIL
```

O YAML acima fornecerá um objeto `address` com o atributo` line1` definido como `711 3rd Ave S`. Isso ocorre porque a chave `line1` no YAML acima é duplicada e, portanto, o último valor de` line1` será retido. O analisador YAML não irá reclamar disso. No entanto, se sua lógica de negócios exigir que você valide o YAML para essas duplicatas, você ainda poderá usar a opção `allowDuplicates` do objeto` AnterosYamlConfig`. A seguir, é feito o seguinte:

```java
    try {
        AnterosYamlConfig yamlConfig = new AnterosYamlConfig();
        yamlConfig.setAllowDuplicates(false); // valor padrão é true
        AnterosYamlReader reader = new AnterosYamlReader(new FileReader("contatos.yml"), yamlConfig);
        Object object = reader.read();
        System.out.println(object);
        Map map = (Map)object;
        System.out.println(map.get("endereco"));
    } catch (YamlException ex) {
        ex.printStackTrace();
        // ou lide com casos-chave duplicados aqui de acordo com sua lógica de negócios
    }
```


## Architecture

AnterosYaml suporta YAML versão 1.0 e 1.1.

