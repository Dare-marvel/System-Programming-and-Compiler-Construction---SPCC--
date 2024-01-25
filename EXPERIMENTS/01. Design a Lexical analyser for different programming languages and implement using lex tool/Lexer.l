%{
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>

enum {
    START, IN_ID, IN_NUM, IN_COMMENT, IN_STRING, IN_INDENTATION
};

int state = START;
%}

%option noyywrap

%%
"if"|"else"|"while"|"for"|"return"|"elif" { printf("KEYWORD: (%s)\n", yytext); state = START; }
"{"|"}"|"("|")"|";"|"["|"]" { printf("SEPARATOR: (%s)\n", yytext); state = START; }
"=="|">="|"<="|">"|"<"|"<>"|"!="|"and"|"or"|"+"|"-"|"*"|"/"|"=" { printf("OPERATOR: (%s)\n", yytext); state = START; }
[0-9]+(\.[0-9]+)?([eE][-+]?[0-9]+)? { printf("NUMBER: (%s)\n", yytext); state = START; }
[a-zA-Z_][a-zA-Z0-9_]* {
    if (strcmp(yytext, "if") != 0 &&
        strcmp(yytext, "else") != 0 &&
        strcmp(yytext, "while") != 0 &&
        strcmp(yytext, "for") != 0 &&
        strcmp(yytext, "return") != 0 &&
        strcmp(yytext, "elif") != 0) {
        printf("IDENTIFIER: (%s)\n", yytext);
    } else {
        printf("KEYWORD: (%s)\n", yytext);
    }
    state = START;
}
\"([^"\\]|\\.)*\" { printf("STRING: (%s)\n", yytext); state = START; }
#.* { printf("COMMENT: (%s)\n", yytext); state = START; }
[ \t] ; // Skip whitespace
\n { printf("SEPARATOR: (NEWLINE)\n"); state = START; } // Treat newline as a separator
: {
    printf("INDENTATION_START: (%s)\n", yytext);
    state = START;
}
. {
    // Handle transitions
    switch (state) {
        case START:
            if (*yytext == '#') {
                state = IN_COMMENT;
            } else if (*yytext == '"') {
                state = IN_STRING;
            } else if (isdigit(*yytext)) {
                state = IN_NUM;
            } else if (isalpha(*yytext) || *yytext == '_') {
                state = IN_ID;
            } else if (*yytext == '.') {
                printf("SEPARATOR: (%s)\n", yytext);
                state = START;
            } else if (*yytext == '+' || *yytext == '-' || *yytext == '*' || *yytext == '/') {
                printf("OPERATOR: (%s)\n", yytext);
                state = START;
            } else {
                printf("INVALID: (%s)\n", yytext);
            }
            break;
        case IN_ID:
            if (!(isalnum(*yytext) || *yytext == '_')) {
                unput(*yytext);
                state = START;
            }
            break;
        case IN_NUM:
            if (!isdigit(*yytext) && *yytext != '.') {
                unput(*yytext);
                state = START;
            }
            break;
        case IN_COMMENT:
            if (*yytext == '\n') {
                state = START;
            }
            break;
        case IN_STRING:
            if (*yytext == '"') {
                state = START;
            }
            break;
    }
}
%%
int main() {
    yylex();
    return 0;
}
