import java.io.*;
import java.text.*;
/**
 *  <code>KeyIn</code> は，キーボードから
 *  Pointを読み込むためのクラスである．
 *
 * @author  Shogo Matsui
 * @version 0.01, 11/14/2015
 */
public class KeyIn {
  /**
   * キーボード入力用reader
   */
  private BufferedReader breader;
  /**
   * 入力変換用DecimalFormat
   */
  private DecimalFormat dformat;

  /**
   *  BufferReaderとDecimalFormatを準備する．
   */
  public KeyIn() {
    breader = new BufferedReader( //キーボード入力用のBuffereReaderを初期化
                    new InputStreamReader(System.in));
    dformat = new DecimalFormat(); //数値変換用オブジェクトを初期化
  }
  /**
   * 次のPointをキーボードから読み込む
   * @return 読み込んだPoint
   */
  public Point readpoint() {
    int innum = readnum(); //2桁の整数を読み込み，
    return new Point(innum / 10, innum % 10); //Pointを作って返す
  }      
  /**
   * 座標を表す2桁の整数をキーボードから読み込む．
   * @return 読み込んだ整数
   */
  private int readnum() {
    try {
      String str = breader.readLine(); //readerから文字列として読み込み,
      Number num = dformat.parse(str); //それをNumberに変換，
      return num.intValue(); //さらにintに直して返す
    } catch(IOException e) { //エラーのときは 0 を返すだけ
      return 0;
    } catch(ParseException e) {
      return 0;
    }
  }
}
