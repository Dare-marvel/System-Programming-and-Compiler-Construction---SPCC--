# Steps to run it -->

1. **Generate Lexer:**
   ```bash
   flex sdt_lex.l
   ```
   - Utilize Flex to process lexical rules in `sdt_lex.l`.
   - Generates a C program (`lex.yy.c`) as the lexer.

2. **Generate Parser:**
   ```bash
   yacc -d sdt_yacc.y
   ```
   - Use Yacc to process grammar rules in `sdt_yacc.y`.
   - The `-d` option generates a header file (`y.tab.h`) alongside the parser code (`y.tab.c`).

3. **Compile Lexer and Parser:**
   ```bash
   gcc lex.yy.c y.tab.c -o sdt_parser -ll
   ```
   - Compile the generated lexer and parser codes.
   - The `-o` option specifies the output executable as `sdt_parser`.
   - The `-ll` flag links the lexer with the Flex library.

4. **Run the Program:**
   ```bash
   ./sdt_parser
   ```
   - Execute the compiled parser program.
   - Allows processing input based on the specified lexical and grammar rules.
   - Obtain the output of the syntax-directed translation as defined in the lexer and parser files.
