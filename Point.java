/**
 *  <code>Point</code> は，座標を表現するクラスである．
 *
 * @author  Shogo Matsui
 * @version 0.031, 11/6/2018
 */
public class Point {
  /**
   * Point が表すx座標．
   */ 
  public int x;
  /**
   * Point が表すy座標．
   */ 
  public int y;
  /**
   * 2つの整数引数からPointを作る．
   * @param   xx  x座標
   * @param   yy  y座標
   */
  public Point(int xx, int yy) {
    x = xx;
    y = yy;
  }
  /**
   * 2つのPointの等価性を調べる．
   * @param   po  比較するPoint
   * @return  true or false
   */
  public boolean equals(Point po) {
    if(po.x == x && po.y == y)
      return true;
    else
      return false;
  }
  /**
   * 座標値を文字列に変換する．
   * @return  座標値を表す文字列
   */
  @Override
  public String toString() {
    return("("+ x +","+ y + ")");
  }
  /**
   * "パス"を表すPointデータ(Point(0,0))．
   */
  public static Point PASS = new Point(0,0);
  /**
   * "待った"を表すPointデータ(Point(9,9))．
   */
  public static Point MATTA = new Point(9,9);
}
