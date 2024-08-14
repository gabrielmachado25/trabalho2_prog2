
// AQUI SELECIONO A PASTA ONDE ESTÁ LOCALIZADO O ARQUIVO
package notepad;

// AQUI DESCREVO AS IMPORTAÇÕES
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

// Classe principal que representa a aplicação (Notepad), estende JFrame para criar a janela da aplicação
public class Notepad extends JFrame { //extend de JFrame
    private JTextArea textArea;
    private JLabel statusLabel;
    private boolean isModified = false;
    private File currentFile = null;
    
// Construtor que configura o título da janela, tamanho e define a operação padrão quando a janela é fechada

    public Notepad() {
        setTitle("Notepad Simulator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

// Inicializa a barra de menus onde teremos os menus
        JMenuBar menuBar = new JMenuBar();
    // Criar o menu Arquivo e adicionar as opções "Abrir", "Salvar" e "Sair", associando ações a essas opções
        JMenu arquivoMenu = new JMenu("Arquivo");
        JMenuItem abrirItem = new JMenuItem("Abrir");
        JMenuItem salvarItem = new JMenuItem("Salvar");
        JMenuItem sairItem = new JMenuItem("Sair");

        abrirItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirArquivo(); //opção abrir
            }
        });

        salvarItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarArquivo(); //opção salvar
            }
        });

        sairItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sair(); //opção sair
            }
        });
        // Adicionando...
        arquivoMenu.add(abrirItem);
        arquivoMenu.add(salvarItem);
        arquivoMenu.addSeparator();
        arquivoMenu.add(sairItem);

    // Criar o menu Fontes e adicionar as opções: Arial -> tamanho 14) e Courier New -> tamanho 24
        JMenu fontesMenu = new JMenu("Fontes");
        JMenuItem arialItem = new JMenuItem("Arial (tamanho 14)");
        JMenuItem courierNewItem = new JMenuItem("Courier New (tamanho 24)");

        arialItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                aplicarFonte(new Font("Arial", Font.PLAIN, 14));
            }
        });

        courierNewItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                aplicarFonte(new Font("Courier New", Font.PLAIN, 24));
            }
        });
        // Adicionando...
        fontesMenu.add(arialItem);
        fontesMenu.add(courierNewItem);

    // Criar o menu Ajuda e adicionar a opção Sobre
        JMenu ajudaMenu = new JMenu("Ajuda");
        JMenuItem sobreItem = new JMenuItem("Sobre");

        sobreItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarSobre();
            }
        });
        // Adicionando...
        ajudaMenu.add(sobreItem);
        
        menuBar.add(arquivoMenu);
        menuBar.add(fontesMenu);
        menuBar.add(ajudaMenu);

        setJMenuBar(menuBar);

    /* Inicializa a área de texto e barra de status criando uma área de texto (JTextArea) para edição do texto, configurando
    a fonte padrão, adicionando um ouvinte de documento para rastrear modificações no texto e criando uma etiqueta (JLabel) para
    exibir a contagem de caracteres e o status de modificações.*/
        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                isModified = true;
                atualizarStatusBar();
            }

            public void removeUpdate(DocumentEvent e) {
                isModified = true;
                atualizarStatusBar();
            }

            public void insertUpdate(DocumentEvent e) {
                isModified = true;
                atualizarStatusBar();
            }
        });

        // Inicializa a barra de status
        statusLabel = new JLabel("Contagem de Caracteres: 0   Modificado: Não");
// Colocando o meu nome embaixo para demonstrar quem desenvolveu o programa
JLabel desenvolvidoPorLabel = new JLabel("Desenvolvido por Gabriel Machado");
desenvolvidoPorLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centraliza o texto

Container contentPane = getContentPane();
contentPane.add(new JScrollPane(textArea), BorderLayout.CENTER);
contentPane.add(statusLabel, BorderLayout.SOUTH);
contentPane.add(desenvolvidoPorLabel, BorderLayout.PAGE_END); // Adiciona abaixo da barra de status


    }
    /* Método abrir arquivo abre uma caixa de diálogo para escolher um arquivo, lê o conteúdo do arquivo selecionado,
    exibe ele na área de texto e atualiza o estado do arquivo atual e modificações.*/
    private void abrirArquivo() {
        JFileChooser fileChooser = new JFileChooser();
        int resultado = fileChooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File arquivo = fileChooser.getSelectedFile();

            try {
                BufferedReader leitor = new BufferedReader(new FileReader(arquivo));
                StringBuilder conteudo = new StringBuilder();
                String linha;

                while ((linha = leitor.readLine()) != null) {
                    conteudo.append(linha).append("\n");
                }

                leitor.close();
                textArea.setText(conteudo.toString());
                currentFile = arquivo;
                isModified = false;
                atualizarStatusBar();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /* Método salvar arquivo abre uma caixa de diálogo para escolher onde salvar o arquivo, caso não hava um arquivo atual,
    salva o conteúdo da área de texto no arquivo e atualiza o estado do arquivo atual e modificações.*/
    private void salvarArquivo() {
        if (currentFile == null) {
            JFileChooser fileChooser = new JFileChooser();
            int resultado = fileChooser.showSaveDialog(this);

            if (resultado == JFileChooser.APPROVE_OPTION) {
                currentFile = fileChooser.getSelectedFile();
            } else {
                return;
            }
        }

        try {
            BufferedWriter escritor = new BufferedWriter(new FileWriter(currentFile));
            escritor.write(textArea.getText());
            escritor.close();
            isModified = false;
            atualizarStatusBar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /* Método sair verifica se o texto foi modificado, exibe uma caixa de diálogo de confirmação para salvar antes
    de sair e fecha a aplicação se o usuário confirmar.*/
    private void sair() {
        if (isModified) {
            int opcao = JOptionPane.showConfirmDialog(this, "O texto foi modificado. Deseja salvar antes de sair?", "Confirmação", JOptionPane.YES_NO_CANCEL_OPTION);

            if (opcao == JOptionPane.YES_OPTION) {
                salvarArquivo();
                dispose();
            } else if (opcao == JOptionPane.NO_OPTION) {
                dispose();
            }
        } else {
            dispose();
        }
    }
    /* Método aplicar fonte permite que o usuário aplique diferentes fontes à área de texto, como "Arial (tamanho 14)"
    ou "Courier New (tamanho 24)"*/
    private void aplicarFonte(Font fonte) {
        textArea.setFont(fonte);
    }
    /* Método mostrar sobre exibe uma caixa de diálogo com informações sobre o aplicativo, como a versão.*/
    private void mostrarSobre() {
        JOptionPane.showMessageDialog(this, "Notepad Simulator\nVersão 1.0", "Sobre", JOptionPane.INFORMATION_MESSAGE);
    }
    /* Método atualizar status bar atualiza a barra de status exibindo a contagem de caracteres e se o texto foi
    modificado desde o último salvamento.*/
    private void atualizarStatusBar() {
        int numCaracteres = textArea.getText().length();
        String modificado = isModified ? "Sim" : "Não";
        statusLabel.setText("Contagem de Caracteres: " + numCaracteres + "   Modificado: " + modificado);
    }
    /* Método main é a entrada da aplicação, onde cria uma instância de NotepadSimulator e a torna visível
    na thread de interface gráfica.*/
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Notepad().setVisible(true);
            }
        });
    }
}
