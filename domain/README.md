# Lexical analysis

The [App](lexical\src\main\java\com\domain\compiler\App.java) class (under lexical package) should be used to run the lexical analysis for the 2024.2 compiler.

The CLI arg should pass the absolute  file path of the source code in order to run it.

JDK version used is 17, you can use [Amazon Coretto](https://aws.amazon.com/pt/about-aws/whats-new/2021/09/amazon-corretto-17-now-available/) for this.

**Example**

```bash
java com.domain.java App D:/absolute/path/to/my/source-code/source.txt
```

The output follows either of:

**Error**

```json
{
  "success" : false,
  "message" : "ERROR: linha 7: comentário de bloco inválido ou não finalizado",
  "tokens" : [ ]
}
```

**Success**

```json
{
  "success" : true,
  "message" : "SUCCESS: Tokens created",
  "tokens" : [ {
    "id" : 16,
    "lexeme" : "b_myBoolean",
    "lineNumber" : 1
  }, {
    "id" : 18,
    "lexeme" : "1,0",
    "lineNumber" : 3
  }, {
    "id" : 18,
    "lexeme" : "2,0",
    "lineNumber" : 4
  }, {
    "id" : 17,
    "lexeme" : "123",
    "lineNumber" : 5
  } ]
}
```
