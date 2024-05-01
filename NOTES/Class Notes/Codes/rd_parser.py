import pprint

class RecursiveDescentParser:
    def __init__(self, input_string):
        self.tokens = self.tokenize(input_string)
        self.current_token_index = 0

    def tokenize(self, input_string):
        return input_string.replace('(', ' ( ').replace(')', ' ) ').split()

    def parse(self):
        return self.parse_E()

    def match(self, expected_token):
        if self.current_token_index < len(self.tokens) and self.tokens[self.current_token_index] == expected_token:
            self.current_token_index += 1
            return True
        return False

    def parse_E(self):
        result = {'E': []}
        result['E'].append(self.parse_T())
        while self.match('+'):
            result['+'] = True
            result['E'].append(self.parse_T())
        return result

    def parse_T(self):
        result = {'T': []}
        result['T'].append(self.parse_F())
        while self.match('*'):
            result['*'] = True
            result['T'].append(self.parse_F())
        return result

    def parse_F(self):
        result = {'F': ''}
        if self.match('('):
            result['('] = True
            result['expression'] = self.parse_E()
            self.match(')')
        elif self.match('id'):
            result['id'] = True
        else:
            raise SyntaxError("Unexpected token: {}".format(self.tokens[self.current_token_index]))
        return result

# Example usage:
input_expr = "id + id * id"
parser = RecursiveDescentParser(input_expr)
parsed_tree = parser.parse()

# Use pprint for a nicer output
pprint.pprint(parsed_tree)
