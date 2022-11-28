package PokemonPanels;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

public class PokemonPanels extends JFrame implements ActionListener{

    final int easy[] = {8, 50};     // 難易度別{1列のセルの数、セル一辺の長さ}
    final int normal[] = {16, 30};
    final int hard[] = {24, 20};

    int btn;                        // セル番号
    int key;                        // 押されたキー情報
    int i, j, k;                    // カウンタ変数
    int level[] = new int[3];       // レベル設定
    int cell;                       // 1列のセルの数
    int cells;                      // ボード全てのセルの数
    int side;                       // セル一辺の長さ
    String previousCmd;             // 前のコマンド記憶
    int previousBtn;                // １ターン前のボタン
    int previousPoke;               // １ターン前のポケモン
    boolean turnFlg;                // ターン制御フラグ
    ArrayList<String> pokemonsCmd;  // ポケモンコマンド配列
    ArrayList<String> pokemons1;    // ポケモンコマンド配列1
    ArrayList<String> pokemons2;    // ポケモンコマンド配列2
    HashMap<String, ImageIcon> pokemonIcons; // ポケモンアイコンハッシュマップ
    ImageIcon icons[];              // ポケモン絵型配列
    JButton button[];               // ボードの生成
    JButton rButton;                // リセットボタン
    JLabel countLabel;              // ラベルの生成
    StartMenu sm;                   // StartMenu再表示
    String cmd;                     // ActionCommandを格納する変数
    JPanel panel = new JPanel();    // パネルを利用;
    File imagesDirectory;           // imagesディレクトリ        
    File[] pokemonPictures;         // ポケモンimageファイル数
    Timer timer;                    // 遅延変数
    TimerTask task;                 // タイマータスク

    // 開発用に作成（実際のプレーではインスタンス化して使用するのでここから起動しない）
    public static void main(String[] args) {
        int inputNum = 1;
        PokemonPanels frame = new PokemonPanels("PokemonPanels", inputNum);
        frame.setVisible(true);
    }

    PokemonPanels(String title, int inputNum){
        switch(inputNum){
            case 1:
                level = easy;
                break;
            case 2:
                level = normal;
                break;
            case 3:
                level = hard;
                break;
        }
        
        // レベル別に設定できるように作り直し
        cell = level[0];                // 1列のセルの数
        cells = cell * cell;            // ボード全てのセルの数
        side = level[1];                // セル一辺の長さ
        button = new JButton[cells];    // ボードの生成
        countLabel = new JLabel("ペアになった数");
        previousCmd = "";               // 前コマンドの初期化
    
        setTitle(title);
        setSize(cell * side + 10, cell * side + 80);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        init();

        getContentPane().add(countLabel, BorderLayout.PAGE_START);
        getContentPane().add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    void init(){
        panel.setLayout(null);

        rButton = new JButton();
        rButton.setBackground(Color.ORANGE);
        rButton.setBounds(cell * side - 100, 3, 70, 20);
        rButton.addActionListener(this);
        rButton.setActionCommand("reset");
        rButton.setText("reset");
        panel.add(rButton);

        pokemons1 = new ArrayList<String>();   // ポケモン配置するために必要
        pokemons2 = new ArrayList<String>();   // ポケモン配置するために必要

        for(int i = 1 ; i <= (cells / 2) ; i++) {
            pokemons1.add("a" + i);
            pokemons2.add("b" + i);
        }

        Collections.shuffle(pokemons1);         // ArrayListをシャッフル
        Collections.shuffle(pokemons2);         // ArrayListをシャッフル

        pokemonsCmd = new ArrayList<String>();

        pokemonsCmd.addAll(pokemons1);
        pokemonsCmd.addAll(pokemons2);

        pokemonIcons = new HashMap<String, ImageIcon>();     // pokemon1とpokemon2を結合するための配列

        for(int i = 0 ; i < (pokemonsCmd.size() / 2) ; i++) {
            pokemonIcons.put(pokemons1.get(i), new ImageIcon("./PokemonPanels/images/" + Integer.parseInt(pokemons1.get(i).substring(1)) + ".png"));
            pokemonIcons.put(pokemons2.get(i), new ImageIcon("./PokemonPanels/images/" + Integer.parseInt(pokemons2.get(i).substring(1)) + ".png"));
        }

        imagesDirectory = new File("./PokemonPanels/images/");
        pokemonPictures = imagesDirectory.listFiles();

        for(i=0; i<cell; i++){
            for(j=0;j<cell;j++){
                btn = i * cell + j;                           // 0〜exまでの番号
                button[btn] = new JButton();
                button[btn].setOpaque(true);        // セルの透明性の有効
                button[btn].setBackground(Color.LIGHT_GRAY);
                button[btn].setBounds(i*side, j*side + 30, side, side);
                button[btn].addActionListener(this);
                button[btn].setActionCommand(pokemonsCmd.get(btn) + "k" + btn);
                panel.add(button[btn]);
                // 【デバッグ用】ボタンに配置された文字列表示　コメントアウトする
                // button[btn].setIcon(icons[btn]);
                // button[btn].setText(pokemons.get(btn));
            }
        }
    }

    public void faceUpButton(int btnNum, int actNum){
        button[btnNum].setIcon(
            IconEditor.resizeIcon(new ImageIcon("./PokemonPanels/images/" + actNum + ".png"),
            side-1, side-1));
    }

    public void faceDownButton(int btnNum, int previousBtn){
        button[previousBtn].setIcon(null);
        button[btnNum].setIcon(null);
    }

    public void delayMethod(int btnNum, int previousBtn){
        timer = new Timer(false);
		task = new TimerTask() {
			@Override
			public void run() {
                faceDownButton(btnNum, previousBtn);
				timer.cancel();
			}
		};
		timer.schedule(task, 1000);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        cmd = e.getActionCommand();
        // System.out.println("cmd = " + cmd);

        int index = cmd.indexOf("k");
        int actNum = Integer.parseInt(cmd.substring(1, index));
        int btnNum = Integer.parseInt(cmd.substring(index + 1));

        if(!previousCmd.equals(cmd)){
            for(String pokeKey: pokemonIcons.keySet()){ 
                if(turnFlg){
                    faceUpButton(btnNum, actNum);
                    if(previousPoke == actNum){
                        button[previousBtn].setEnabled(false);
                        button[btnNum].setEnabled(false);
                        button[previousBtn].setBackground(Color.GRAY);
                        button[btnNum].setBackground(Color.GRAY);
                    }else{
                        delayMethod(btnNum, previousBtn);
                    }
                    turnFlg = false;
                    break;
                } else {
                    if(actNum == Integer.parseInt(pokeKey.substring(1))){
                        faceUpButton(btnNum, actNum);
                        turnFlg = true;
                        break;
                    }
                }
            }
            previousCmd = cmd;
            previousBtn = btnNum;
            previousPoke = actNum;
        }
    }
}