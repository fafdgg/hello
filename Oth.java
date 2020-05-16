/**
 *  <code>Oth</code> は，オセロのメインメソッドを含むクラスである．
 *
 * @author  Shogo Matsui
 * @version 0.04, 11/15 2015
 */
public class Oth {
  /**
   * versose オプション用のフラグ.
   *  trueの場合にはデバッグ情報を印刷する．
   */
  static public boolean vflag = false;       //verboseフラグ

  /**
   *  メインメソッド．
   *   使用方法 Oth [-verbose] [-wb]<br>
   *       -verbose  デバッグ情報の表示，
   *       -w        白番がコンピュータ，
   *       -b        黒番がコンピュータ．
   */
  public static void main(String[] args) {

    boolean wflag = false;     // 白番プレーヤ（デフォルトは人間）
    boolean bflag = false;     // 黒番プレーヤ（デフォルトは人間）

    Board[] barr = new Board[120];  // Board を保存する配列を用意
    int barr_pt = 0;                // 配列のインデックス用の変数

    KeyIn kin = new KeyIn(); // Key入力用オブジェクト
    Board bd  = new Board(); // Boardデータ用
    Point newpt;             // Point一次保存用

    /* オプション引数の処理 */
    int i = 0;
    while (i < args.length && args[i].startsWith("-")) { // "-"で始まる場合
      String arg = args[i++];  //　文字列を取り出す

      /* verbose flag check */
      if(arg.equals("-verbose")) {   // -verbose ならば
        System.out.println("verbose mode on");  //表示してから
        vflag = true;               // vflagをtrueに
      }

      /* 1 character flag chack */
      else {
        for(int j = 1; j < arg.length(); j++) { //1文字ずつ
          char flag = arg.charAt(j);  // flagに取り出す
          switch(flag) {
          case 'w':   // wフラグの処理
            wflag = true;  // wflagをtrueに
            break;
          case 'b':   // bフラグの処理
            bflag = true;  // bflagをtrueに
            break;
          default:    // それら以外はエラーメッセージを出す
            System.err.println("Oth: illegal option " + flag);
            break;
          }
        }
      }
    }
    if(i != args.length)  // "-"で始まらない引数が残っている場合
      System.err.println("Usage: Oth [-verbose] [-wb]");
    else // vflagがtrue ならば情報を表示する 
      if(vflag)
        System.out.println(
          "options: verbose=" + vflag + " b=" + bflag + " w=" + wflag);

    /* main loop */
    label1:
    do {
      barr[barr_pt++] = bd; // Board を一手ごとに保存しておく

      bd.printboard(); //boardの印刷
      if(vflag) bd.printnextmove(); //次に打てる場所を印刷(デバッグ)

      if((bd.isturnblack() && !bflag)
         || (!bd.isturnblack() && ! wflag)) { 
        /* 黒番で bflag==false または 白番で wflag==false のときは，
           人間が打つ */   
        do {
          if(vflag) {  //コンピュータが計算した次の手を印刷(デバッグ)
            newpt = bd.findbesthand();
            System.out.println(" " + newpt);	
          }
          System.out.print("Next move: "); //プロンプトを表示して
          newpt = kin.readpoint(); //次の手の入力
          if (newpt.equals(Point.MATTA)) { //待ったの処理
            if(barr_pt >= 3) {     // 3手め以降ならば戻せる
              bd = barr[barr_pt - 3]; // 配列から前の状態を取りだす
              barr_pt -= 3;  // インデックスを戻す
              continue label1;  // label1 から処理を再開
            }
          }
        } while(bd.ispointok(newpt) == false); //手が正しくない場合は繰り返す
      } else {
        newpt = bd.findbesthand();
        System.out.println("Next move: " + newpt + " selected");
      }
      bd = bd.domove(newpt); //手を打って，新しいBoardを作る
    } while(bd.isgameover() == false); //ゲームオーバーでない限りつづける
    bd.printboard(); //終了時の盤面を表示する
  }
}
