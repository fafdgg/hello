/**
 *  <code>Board</code> は，オセロの盤面を表現するクラスである．
 *  ver.2 evalw を加えたバージョン
 *
 * @author  Shogo Matsui
 * @version 0.032, 11/10/2014
 */
public class Board {
    private int[][] board;  
    private Nextmove nextmv;
    private int hand; /* hand 1, -1 */
    private int depth;

    private static final int[][] dxdy = {{ 1,1},{ 1,0},{ 1,-1},
					 { 0,1},       { 0,-1},
					 {-1,1},{-1,0},{-1,-1} }; 
    /**
     * ゲーム開始時のBoardを作成する．
     * ゲーム盤の配列(初期状態)，手数(4)，手番(黒)，
     * 次に打てる場所のデータNextmoveを初期化する．
     */
    public Board() {
	int[][] bd  = {{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		       { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		       { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		       { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		       { 0, 0, 0, 0,-1, 1, 0, 0, 0, 0},
		       { 0, 0, 0, 0, 1,-1, 0, 0, 0, 0},
		       { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		       { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		       { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		       { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0} };
	board = bd;
	hand = 1;
	depth = 4;
	nextmv = new Nextmove(bd, hand);
    }
    /**
     * 配列データから新たなBoardを作成する
     * @param bd 新たなボードの配列データ
     * @param next 次のプレーヤ
     * @param dp 手数
     */
    public Board(int[][] bd, int next, int dp) {
	board = bd;
	hand = next;
	depth = dp;
	nextmv = new Nextmove(bd, hand);
    }
    /**
     * 黒の手番ならば真を返す．
     * @return 黒番ならばtrue
     */
    public boolean isturnblack() {
	return hand == 1;
    }
    /**
     * 手の正当性をチェックする(nextmvに含まれているかどうか調べる)．
     * パスの場合( (0,0) )も，打つ場所がないことを確認する．
     * @param next 石を打つ場所
     * @return 正しければtrue
     */
    public boolean ispointok(Point next) {
	if(nextmv.length() == 0 && next.equals(Point.PASS))
	    return true;
	return nextmv.search(next);
    }
    /**
     * ゲームが終了しているが調べる．
     * @return 終わりであればtrue
     */
    public boolean isgameover() {
	if(depth == 64)
	    return true; // 64手打った．
	if(nextmv.length() != 0)
	    return false; // 打つ手がある．
	else {
	    Board nbd = domove(Point.PASS); // パスする.
	    if(nbd.nextmv.length() == 0) // さらにパスか？
		return true;
	}
	return false;
    }

    /**
     * 石を打ち，次の盤面を作る．
     * @param nextPt 石を打つ場所
     * @return 新たな盤面(Boardオブジェクト)
     */
    public Board domove(Point nextPt) {
	int[][] nextbd = new int[10][10];
	/* 配列のコピー */
	for(int i = 0; i < 10; i++) {
	    for(int j = 0; j < 10; j++) {
		nextbd[i][j] = board[i][j];
	    }
	}
	/* 石を置いてひっくり返す */
	int nx = nextPt.x;
	int ny = nextPt.y;
	if ((nx == 0) && (ny == 0))  // 次の手が(0,0)の場合はパス
	    return new Board(nextbd, -hand, depth) ; // handだけ変える
	/* 石を置く*/
	nextbd[nx][ny] = hand; // 石をnextPtの位置に打つ
	/* 石をひっくり返す */
	for(int dir = 0; dir < 8; dir++) { //8つの方向について以下を行う
	    int dx = dxdy[dir][0];     //x,yの増分データを配列からとり出す
	    int dy = dxdy[dir][1];
	    int m = dx;                //増分データをm,nにセット
	    int n = dy;
	    while(hand + board[nx+m][ny+n] == 0) { //相手の石がある限り
		m += dx; n += dy;        //m,nを更新
	    }
	    if(hand == board[nx+m][ny+n]) {   //その先に自分の石があれば
		m -= dx; n -= dy;
		while(hand + board[nx+m][ny+n] == 0) { //逆戻りしながら
		    nextbd[nx+m][ny+n] = hand;  //自分の石に変えていく
		    m -= dx; n -= dy;
		}
	    }
	}
	return new Board(nextbd, -hand, depth + 1);
    }
    /**
     * Boardを表示する．
     */
    public void printboard() {
	char ch = ' ';
	System.out.println();
	if(hand == -1)
	    ch = 'O';
	else
	    ch = 'X';
	System.out.println("+++ " + ch + "'s turn! ++ depth= " + depth);
	System.out.println("  1 2 3 4 5 6 7 8");
	int xscore = 0, oscore = 0;
	for(int j = 1; j <= 8; j++) {
	    System.out.print("" + j);
	    
	    for(int i = 1; i <= 8; i++) {
		if(board[i][j] == 0)
		    ch = '.';
		if(board[i][j] == 1) {
		    ch = 'X';
		    xscore++;
		}
		if(board[i][j] == -1) {
		    ch = 'O';
		    oscore++;
		}
		System.out.print(" " + ch);
	    }
	    System.out.println();
	}
	System.out.println("score: O=" + oscore + ", X=" + xscore);
    }
    /**
     * 次に打てる場所(Nextmove)を表示する(デバッグ用)．
     */
    public void printnextmove() {
	System.out.println(nextmv);
    }
    /**
     * 次に打つべき手を探す．
     * @return 次に打つ場所
     */
    public Point findbesthand() {
	if(depth <= 46)//46
	    return nvalw(); //nvalw(); evalbd(); 
	else if(depth <= 52)//52
	    return ivalw(); //ivalw();
	else
	    return evalw(); //evalw();return nextmv.getrand();
	//次に打つ場所を決めて，Pointを返す．
	//たとえば，
	//5-49手は，ランダムに手を決める，
	//50-64手は，evalw() を呼ぶ， など．

    }
    
    /**
     * evalw は，必勝手を選択する．
     * 必勝手が見つからない場合はランダムに手を選択する．
     * @return  次の手のPoint
     */
    private Point evalw() {
	Point pnt;
	if(nextmv.length() == 0)
	    return Point.PASS ; /* パス */
	nextmv.resetPt(); // getsucの初期化
	if(nextmv.length() == 1)
	    return nextmv.getsuc(); // 先頭を返す 
	while((pnt = nextmv.getsuc()) != null) { //nextmvの各Pointについて
	    // System.out.print("" + pnt ); //処理中のPointの印刷(デバッグ用)
	    Board nextbd = domove(pnt); //打って次の盤面を作る
	    if(nextbd.bevalw() == false) { //勝つ場所が見つかったら
		System.out.print("win ");     //"win" と印刷(デバッグ用)
		return pnt;  //位置情報(Point)を返す
	    }
	}
	/* 負けの場合 */
	System.out.print("lose "); //"lose" と印刷(デバッグ用)
	return nvalw(); //ランダムに選ぶ
    }
    /**
     * bevalw は，盤面の勝ち負けを判定する．
     * @return  勝ちのとき true  負けのとき false
     */
    private boolean bevalw() {
	Point pnt;
	if(nextmv.length() > 0) { //パスでない場合
	    nextmv.resetPt(); // getsucの初期化
	    while((pnt = nextmv.getsuc()) != null) { //nextmvの各Pointについて
		Board nextbd = domove(pnt); //打って次の盤面を作る
		if (nextbd.bevalw() == false)
		    return true; //勝つ場所があった
	    }
	    return false;  //勝つ場所がなかった
	} else if (!isgameover()) { // パスの場合
	    Board nextbd = domove(Point.PASS);  //パスとして新しい盤面を作り
	    return !nextbd.bevalw();  // 再帰して，評価値を反転してから返す
	} else {  // ゲームオーバーのとき
	    int score = 0; // スコアを数える
	    for(int i = 1; i <= 8; i++) {
		for(int j = 1; j <= 8; j++) {
		    score += board[i][j];
		}
	    }
	    score = score * hand;
	    if(score > 0)
		return true; // スコアが大きいと勝ち
	    else
		return false; // スコアが小さいか等しいと負け
	}
    }

    private Point ivalw() {
	Point pnt;
	Point y = null;
	int x = -9999;
	int beta = 9999;
	int min = 9999, k=0;
	if(nextmv.length() == 0)
	    return Point.PASS ; /* パス */
	nextmv.resetPt(); // getsucの初期化
	if(nextmv.length() == 1)
	    return nextmv.getsuc(); // 先頭を返す 
	while((pnt = nextmv.getsuc()) != null) { //nextmvの各Pointについて
	    // System.out.print("" + pnt ); //処理中のPointの印刷(デバッグ用)
	    Board nextbd = domove(pnt); //打って次の盤面を作る
	    x = nextbd.bivalw(beta);
	    
	    if(min > x) { //勝つ場所が見つかったら
		min = x;
		beta = min;
		y = pnt;
	    }
	    //System.out.println("x  " + x);
	    //return pnt;  //位置情報(Point)を返す
	    
	}
	//System.out.println("min  " + min);
	//return nextmv.getrand(); //ランダムに選ぶ
	return y;
    }
    private int bivalw(int beta) {
	Point pnt;
	int x = -9999;
	int min = 9999, k=0;
	if(nextmv.length() > 0) { //パスでない場合
	     nextmv.resetPt(); // getsucの初期化
	     while((pnt = nextmv.getsuc()) != null) { //nextmvの各Pointについ
	 	Board nextbd = domove(pnt); //打って次の盤面を作る
		//x = nextbd.bivalw((-1)*beta);
		if(x > beta)
		  return (-1)*min;
	 	x = nextbd.bivalw((-1)*beta);
		//System.out.println("y  " + x);

	 	if (min > x){
	 	    min = x;
		    //return true; //勝つ場所があった
		}
		//if(x > beta)
		//  return (-1)*min;
		//k++;
	    }
	     return (-1)*min;  //勝つ場所がなかった
	} else if (!isgameover()) { // パスの場合
	    Board nextbd = domove(Point.PASS);  //パスとして新しい盤面を作り
	    return nextbd.bivalw((-1)*beta);  // 再帰して，評価値を反転してから返す
	} else {  // ゲームオーバーのとき
	    int score = 0; // スコアを数える
	    for(int i = 1; i <= 8; i++) {
		for(int j = 1; j <= 8; j++) {
			score += board[i][j];
		}
	    }
		score = score * hand;
		//System.out.println("score  " + score);
		return score;
		
	    /*
	      if(score > 0)
	      return true; // スコアが大きいと勝ち
	      else
	      return false; // スコアが小さいか等しいと負け*/

	}
    }
    private Point nvalw() {
	Point pnt;
	Point y = null;
	int x = 0;
	int min = 99999, k = 0;
	if(nextmv.length() == 0)
	    return Point.PASS ; /* パス */
	nextmv.resetPt(); // getsucの初期化
	if(nextmv.length() == 1)
	    return nextmv.getsuc(); // 先頭を返す
	while((pnt = nextmv.getsuc()) != null) { //nextmvの各Pointについて
	    // System.out.print("" + pnt ); //処理中のPointの印刷(デバッグ用)
	    Board nextbd = domove(pnt); //打って次の盤面を作る
	    x = nextbd.bnvalw(5);
	    if(min > x) { //勝つ場所が見つかったら
		min = x;
		y = pnt;
	    }
	    // System.out.print("win ");     //"win" と印刷(デバッグ用)
	    //return pnt;  //位置情報(Point)を返す
	    
	}
	/* 負けの場合 */
	//System.out.print("hand1 " + hand); //"lose" と印刷(デバッグ用)
	//return nextmv.getrand(); //ランダムに選ぶ
	return y;
    }

    private int bnvalw(int n) {
	Point pnt;
	int x = 0;
	int min = 99999, k = 0;
	if(nextmv.length() > 0 && n!=0) { //パスでない場合
	    nextmv.resetPt(); // getsucの初期化
	    while((pnt = nextmv.getsuc()) != null) { //nextmvの各Pointについて
		Board nextbd = domove(pnt); //打って次の盤面を作る
		x = nextbd.bnvalw(n-1);
		if (min > x){
		    min = x;
		    //return true; //勝つ場所があった
		}
	    }
	    
	    return (-1)*min;  //勝つ場所がなかった
	}
	else if(n!=0) { // パスの場合
	    Board nextbd = domove(Point.PASS);  //パスとして新しい盤面を作り
	    return nextbd.bnvalw(n-1);  // 再帰して，評価値を反転してから返す
	}
	else {  // ゲームオーバーのとき
	    int s = 0;
	    while((pnt = nextmv.getsuc()) != null) { //nextmvの各Pointについて
		s++;
	    }
	    int bw = 0;
	    int bh = 0;
	    int bs = 0;
	    int[][] b  = {{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			  { 0, 100, -20, 14, 3, 3, 14, -20, 100, 0},
			  { 0, -20, -50, -2, -2, -2, -2, -50, -20, 0},
			  { 0, 14, -2, 1, 1, 1, 1, -2, 14, 0},
			  { 0, 3, -2, 1, 3, 3, 1, -2, 3, 0},
			  { 0, 3, -2, 1, 3, 3, 1, -2, 3, 0},
			  { 0, 14, -2, 1, 1, 1, 1, -2, 14, 0},
			  { 0, -20, -50, -2, -2, -2, -2, -50, -20, 0},
			  { 0, 100, -20, 14, 3, 3, 14, -20, 100, 0},
			  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0} };
	    int score = 0; // スコアを数える
	    for(int i = 1; i <= 8; i++) {
		for(int j = 1; j <= 8; j++) {
		    bs += board[i][j];
		    if(board[i][j]==hand){
			bh ++;
			bw += b[i][j];
		    }
		}
	    }

	    int fix = 0;
	    boolean flag_up = true;
	    boolean flag_right = true;
	    boolean flag_down = true;
	    boolean flag_left = true;
	    if(board[1][1] == hand) fix++;
	    if(board[1][8] == hand) fix++;
	    if(board[8][8] == hand) fix++;
	    if(board[8][1] == hand) fix++;
	    for(int i = 1; i <= 8; i++) { //辺が全て埋まっている
		if(flag_up == true && board[1][i] == 0) flag_up = false;
		if(flag_right == true && board[i][8] == 0) flag_right = false;
		if(flag_down == true && board[8][i] == 0) flag_down = false;
		if(flag_left == true && board[i][1] == 0) flag_left = false;
	    }
	    if(flag_up == true){
		for(int i = 1; i <= 8; i++) {
		    if(board[1][i] == hand)
			fix++;
		}
	    }
	    else if (flag_up == false) {
		if(board[1][1] == hand){
		    for(int i = 1; i <= 8; i++) {
			if(board[1][i] == 0 || board[1][i] == -hand )
			    break;
			fix++;
		    }
		}
		if(board[1][8] == hand){
		    for(int i = 1; i <= 8; i++) {
			if(board[1][8-i] == 0 || board[1][8-i] == -hand )
			    break;
			fix++;
		    }
		}
	    }
	    else
		System.out.printf("error");
	    
	    if(flag_right == true){
		for(int i = 1; i <= 8; i++) {
		    if(board[i][8] == hand)
			fix++;
		}
	    }
	    else if (flag_right == false) {
		if(board[1][8] == hand){
		    for(int i = 1; i <= 8; i++) {
			if(board[i][8] == 0 || board[i][8] == -hand )
			    break;
			fix++;
		    }
		}
		if(board[8][8] == hand){
		    for(int i = 1; i <= 8; i++) {
			if(board[8-i][8] == 0 || board[8-i][8] == -hand )
			    break;
			fix++;
		    }
		}
	    }
	    else
		System.out.printf("error");
	    
	   if(flag_down == true){
		for(int i = 1; i <= 8; i++) {
		    if(board[8][i] == hand)
			fix++;
		}
	    }
	    else if (flag_down == false) {
		if(board[8][1] == hand){
		    for(int i = 1; i <= 8; i++) {
			if(board[8][i] == 0 || board[8][i] == -hand )
			    break;
			fix++;
		    }
		}
		if(board[8][8] == hand){
		    for(int i = 1; i <= 8; i++) {
			if(board[8][8-i] == 0 || board[8][8-i] == -hand )
			    break;
			fix++;
		    }
		}
	    }
	    else
		System.out.printf("error");

	   if(flag_left == true){
		for(int i = 1; i <= 8; i++) {
		    if(board[i][1] == hand)
			fix++;
		}
	    }
	    else if (flag_left == false) {
		if(board[1][1] == hand){
		    for(int i = 1; i <= 8; i++) {
			if(board[i][1] == 0 || board[i][1] == -hand )
			    break;
			fix++;
		    }
		}
		if(board[8][1] == hand){
		    for(int i = 1; i <= 8; i++) {
			if(board[8-i][1] == 0 || board[8-i][1] == -hand )
			    break;
			fix++;
		    }
		}
	    }
	    else
		System.out.printf("error");

	   int wing = 0;
	   if(board[1][1] == 0 && board[1][2] == hand && board[1][3] == hand &&  board[1][4] == hand && board[1][5] == hand && board[1][6] == hand && board[1][7] == 0 && board[1][8] == 0){
	       wing++;
	   }
	   else if( board[1][1] == 0 && board[1][2] == 0 && board[1][3] == hand &&  board[1][4] == hand && board[1][5] == hand && board[1][6] == hand && board[1][7] == hand && board[1][8] == 0){
	       wing++;
	   }
	   
	   if(board[1][8] == 0 && board[2][8] == hand && board[3][8] == hand &&  board[4][8] == hand && board[5][8] == hand && board[6][8] == hand && board[7][8] == 0 && board[8][8] == 0){
	       wing++;
	   }
	   else if( board[1][8] == 0 && board[2][8] == 0 && board[3][8] == hand &&  board[4][8] == hand && board[5][8] == hand && board[6][8] == hand && board[7][8] == hand && board[8][8] == 0){
	       wing++;
	   }

	   if(board[8][1] == 0 && board[8][2] == hand && board[8][3] == hand &&  board[8][4] == hand && board[8][5] == hand && board[8][6] == hand && board[8][7] == 0 && board[8][8] == 0){
	       wing++;
	   }
	   else if( board[8][1] == 0 && board[8][2] == 0 && board[8][3] == hand &&  board[8][4] == hand && board[8][5] == hand && board[8][6] == hand && board[8][7] == hand && board[8][8] == 0){
	       wing++;
	   }
	   
	   if(board[1][1] == 0 && board[2][1] == hand && board[3][1] == hand &&  board[4][1] == hand && board[5][1] == hand && board[6][1] == hand && board[7][1] == 0 && board[8][1] == 0){
	       wing++;
	   }
	   else if( board[1][1] == 0 && board[2][1] == 0 && board[3][1] == hand &&  board[4][1] == hand && board[5][1] == hand && board[6][1] == hand && board[7][1] == hand && board[8][1] == 0){
	       wing++;
	   }

	   int mount = 0;
	   if (board[1][1] == 0 && board[1][2] == hand && board[1][3] == hand && board[1][4] == hand && board[1][5] == hand && board[1][6] == hand && board[1][7] == hand && board[1][8] == 0 &&
		board[2][1] == 0 && board[2][2] == 0 && board[2][3] != 0 && board[2][4] != 0 &&	board[2][5] != 0 && board[2][6] != 0 && board[2][7] == 0 && board[2][8] == 0){
		mount++;
	   }
	   if (board[1][8] == 0 && board[2][8] == hand && board[3][8] == hand && board[4][8] == hand && board[5][8] == hand && board[6][8] == hand && board[7][8] == hand && board[8][8] == 0 && board[1][7] == 0 && board[2][7] == 0 && board[3][7] != 0 && board[4][7] != 0 && board[5][7] != 0 && board[6][7] != 0 && board[7][7] == 0 && board[8][7] == 0){
		mount++;
	}
	   if (board[8][1] == 0 && board[8][2] == hand && board[8][3] == hand && board[8][4] == hand && board[8][5] == hand && board[8][6] == hand && board[8][7] == hand && board[8][8] == 0 && board[7][1] == 0 && board[7][2] == 0 && board[7][3] != 0 && board[7][4] != 0 && board[7][5] != 0 && board[7][6] != 0 && board[7][7] == 0 && board[7][8] == 0){
		mount++;
	}
	if (board[1][1] == 0 && board[2][1] == hand && board[3][1] == hand && board[4][1] == hand && board[5][1] == hand && board[6][1] == hand && board[7][1] == hand && board[8][1] == 0 &&
		board[1][2] == 0 && board[2][2] == 0 && board[3][2] != 0 && board[4][2] != 0 &&	board[5][2] != 0 && board[6][2] != 0 && board[7][2] == 0 && board[8][2] == 0){
		mount++;
	}

	if(depth <= 35){
	    score = 3*s + 5*fix - 5*wing + 10*mount + 6*bw ;
	}
	else if(depth <=57) {
	    score = 6*s + 10*bw + 15*fix - 15*wing + 15*mount;
	}
	else{
	    score = bs;
	}
	    //score = 5*bw + 10*bh + 3*fix;
	//score = score * hand;
	
	return score;
	
	}
	
    }
    
     private Point evalbd() {
	Point pnt;
	if(nextmv.length() == 0)
	    return Point.PASS ; /* パス */
	nextmv.resetPt(); // getsucの初期化
	if(nextmv.length() == 1)
	    return nextmv.getsuc(); // 先頭を返す 
	int max = -9999;
	Point y = null;
	int[][] b  = {{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			  { 0, 30, -12, 0, -1, -1, 0, -12, 30, 0},
			  { 0, -12, -15, -3, -3, -3, -3, -15, -12, 0},
			  { 0, 0, -3, 0, -1, -1, 0, -3, 0, 0},
			  { 0, -1, -3, -1,-1, -1, -1, -3, -1, 0},
			  { 0, -1, -3, -1, -1,-1, -1, -3, -1, 0},
			  { 0, 0, -3, 0, -1, -1, 0, -3, 0, 0},
			  { 0, -12, -15, -3, -3, -3, -3, -15, -12, 0},
			  { 0, 30, -12, 0, -1, -1, 0, -12, 30, 0},
			  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0} };
	while((pnt = nextmv.getsuc()) != null) { //nextmvの各Pointについて
	    if(b[pnt.x][pnt.y] > max) { //勝つ場所が見つかったら
		max = b[pnt.x][pnt.y];
		y = pnt;  //位置情報(Point)を返す
	    }
	}
	/* 負けの場合 */
	// System.out.print("lose "); //"lose" と印刷(デバッグ用)
	return y; //ランダムに選ぶ
    }


}
