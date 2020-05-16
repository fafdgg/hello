/**
 *  <code>Nextmove</code> は，次に置ける場所を表現するクラスである．
 *
 * @author  Shogo Matsui
 * @version 0.033, 11/6/2018
 */
public class Nextmove {
  private Point[] nextmove = new Point[64]; 
  private int maxpoint = 0;
  private int sucPt = 0;
  private static final int[][] dxdy = {{ 1,1},{ 1,0},{ 1,-1},
                                       { 0,1},       { 0,-1},
                                       {-1,1},{-1,0},{-1,-1} };
  /**
   * 配列データから次に置ける場所(Nextmove)を作成する．
   * @param board ボードの配列データ
   * @param hand プレーヤ
   */
  public Nextmove(int[][] board, int hand) {
    /* nextmove[] に次に打てる場所(Point)を入れる */
    for(int nx = 1; nx <= 8; nx++) { //すべての場所(nx,ny)について調べる
      for(int ny = 1; ny <= 8; ny++) {
        if(board[nx][ny] != 0)
          continue; //すでに石があれば，スキップ

        /* (nx,ny)に石を置くと相手の石がはさめるか調べる */
        for(int dir = 0; dir < 8; dir++) {//forloop1:8つの方向について以下を行う
          int dx = dxdy[dir][0];    //x,yの増分データを配列からとり出す
          int dy = dxdy[dir][1];   
          if(hand + board[nx+dx][ny+dy] == 0) { // 1つ先が相手の石のとき
            int m = 2 * dx, n = 2 * dy; // m, n を２つ先の値に初期化
            while(hand + board[nx+m][ny+n] == 0) { //相手の石がある限り
              m += dx; n += dy;                    //m,nを更新
            }
            if(hand == board[nx+m][ny+n]) {  //その先に自分の石があれば
              nextmove[maxpoint++] = new Point(nx, ny); //nextmoveに登録
              break; // forloop1 を抜け出す
            }
          }
        } 
      }
    }
  }
  /**
   * 呼ばれるたびにPointを順に１つずつ返す．
   * もうなければnullを返す．
   * @return  取り出したポイントまたはnull 
   */
  public Point getsuc() {
    if(sucPt == maxpoint)
      return null;
    return nextmove[sucPt++];
  }
  /**
   * getsucを初期化する(取り出すポイントを最初に戻す)．
   */
  public void resetPt() {
    sucPt = 0;
  }
  /**
   * 次に打てる場所(Point)をランダムに１つ選ぶ．
   * @return  取り出したポイント
   */
  public Point getrand() {
    if(maxpoint == 0)
      return Point.PASS; /* パス */
    /* 0 <= index < maxpoint の index を乱数で生成 */
    int index = (int)(maxpoint * Math.random()); 
    return nextmove[index];
  }
  /**
   * 登録されているPointの個数を返す．
   * @return  Pointの個数
   */
  public int length() {
    return maxpoint;
  }
  /**
   * 同じPointが登録されていれば真を返す．
   * @param   xy 探すPoint
   * @return  見つかればtrue
   */
  public boolean search(Point xy) {
    for(int i = 0; i < maxpoint; i++) {
      if(nextmove[i].equals(xy)) {
        return true;
      }
    }
    return false;
  }
  /**
   * Nextmoveの内容を文字列(String)に変換する．
   * @return  "#個数 -- 座標1座標2...座標n"という形式の文字列 */
  @Override
  public String toString() {
    String returnstr = "#" + maxpoint + " --";
    for(int i = 0; i < maxpoint; i++) {
      returnstr += " " + nextmove[i];
    }
    return returnstr;
  }
}
