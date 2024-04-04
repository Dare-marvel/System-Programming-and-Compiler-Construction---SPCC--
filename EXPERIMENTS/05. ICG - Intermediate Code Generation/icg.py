from prettytable import PrettyTable

print("Enter the postfix expression: ", end="")
postfix_expression = str(input())
operand_stack = []
temp_index = 0
quadruples_table = PrettyTable(["Operator", "Operand1", "Operand2", "Result"])

for token in postfix_expression:
    if token.isalpha():
        operand_stack.append(token)
    else:
        operand2 = operand_stack.pop()
        operand1 = operand_stack.pop()
        temp_index += 1
        temp_result = "t" + str(temp_index)
        operand_stack.append(temp_result)
        quadruples_table.add_row([token, operand1, operand2, temp_result])

print("Operand stack:", operand_stack)
print(quadruples_table)
