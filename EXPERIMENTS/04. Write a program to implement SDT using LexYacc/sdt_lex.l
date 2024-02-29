%{
#include "y.tab.h"
%}

%%
N { return N; }
S { return S; }
E { return E; }
W { return W; }
\n { return 0; }
. { return yytext[0]; }
%%

int yywrap(void) {
    return 1;
}
