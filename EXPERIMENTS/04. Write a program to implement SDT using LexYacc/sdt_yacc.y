%{
#include <stdio.h>
int yylex();
void yyerror(const char *s);
int xpos = 0;
int ypos = 0;
%}

%token N S E W

%%

input: moves { printf("Final position is (%d, %d)\n", xpos, ypos); }
     | moves error { printf("Invalid string\n"); }
     ;

moves: moves move { }
     | move { }
     ;

move: N { ypos++; }
    | S { ypos--; }
    | E { xpos++; }
    | W { xpos--; }
    ;

%%

void yyerror(const char *s) {
    fprintf(stderr, "%s\n", s);
}

int main() {
    printf("Enter input string: ");
    yyparse();
    return 0;
}
