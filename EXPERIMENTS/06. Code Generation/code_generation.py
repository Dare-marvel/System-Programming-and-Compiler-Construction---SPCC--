import networkx as nx
import matplotlib.pyplot as plt


def generate_basic_blocks(tac):
    basic_blocks = []
    block = []


    for line in tac:
        if 'if' in line or 'goto calling program' in line or 'return' in line:
            if block:
                basic_blocks.append(block)
            block = [line]
            basic_blocks.append(block)
            block = []
        else:
            block.append(line)


    if block:
        basic_blocks.append(block)


    return basic_blocks


def generate_control_flow_graph(basic_blocks):
  G = nx.DiGraph()
  G.add_edge('start', 1)
   
  for idx, block in enumerate(basic_blocks):
    last_line = block[-1]
   
    if 'if' in last_line:
      if 'goto' in last_line:
        true_label = last_line[-1]
     
      G.add_edge(idx + 1, basic_blocks.index(block) + 2)
     
      for next_block in basic_blocks:
        if next_block[0][0] == true_label:  
          G.add_edge(idx + 1, basic_blocks.index(next_block) + 1)
     
    elif 'goto' in last_line:
      if 'calling' in last_line and 'program' in last_line:  
        pass  
      else:  
        if '(' or ')' in last_line:
           last_line = last_line.replace('(', '').replace(')', '')
       
        next_block_label = last_line
        for next_block in basic_blocks:
          if next_block[0][0] == next_block_label[-1]:
            G.add_edge(idx + 1, basic_blocks.index(next_block) + 1)


   
 
    else:
      next_block_idx = idx + 1
      if next_block_idx < len(basic_blocks):
        G.add_edge(idx + 1, next_block_idx + 1)


  G.add_edge(idx+1,'end')
  return G


def display_control_flow_graph(G):
    pos = nx.spring_layout(G)
    pos = {node: (y, -x) for node, (x, y) in pos.items()}  # Rotate layout
    nx.draw(G, pos, with_labels=True, node_size=1500, node_color="skyblue", font_size=12, font_weight="bold")
    plt.title("Control Flow Graph")
    plt.show()


def main():
    tac = []
    line_number = 1  
    print("Enter Three Address Code (TAC) line by line. Enter an empty line to stop.")
    while True:
        line = input().strip()
        if not line:
            break


        tac.append(f"{line_number} {line}")
        line_number += 1  
       


    basic_blocks = generate_basic_blocks(tac)
    control_flow_graph = generate_control_flow_graph(basic_blocks)


    print("\nBasic Blocks:")
    for i, block in enumerate(basic_blocks):
        print(f"Block {i + 1}:")
        for code in block:
            print(' '.join(code))
        print()


    print("\nControl Flow Graph:")
    for edge in control_flow_graph.edges():
        print(edge)


    display_control_flow_graph(control_flow_graph)


if __name__ == "__main__":
    main()
