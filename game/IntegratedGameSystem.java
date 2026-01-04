import java.util.*;

public class IntegratedGameSystem {

    // 全域變數：掃描器，用來讀取使用者的鍵盤輸入
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // [Source 45-50] 系統規格：主選單設計
        while (true) { // 無窮迴圈，直到使用者選擇退出
            System.out.println("==================================");
            System.out.println("      整合式電腦遊戲系統 (Term Project)");
            System.out.println("==================================");
            System.out.println("1. 擲骰子遊戲 (Craps)");
            System.out.println("2. 猜數字遊戲 (1A2B)");
            System.out.println("3. 剪刀石頭布 (自行開發遊戲)");
            System.out.println("4. 井字遊戲 (Tic Tac Toe)"); // New game
            System.out.println("5. 結束系統");
            System.out.print("請輸入選項 (1-5): ");

            String input = scanner.next(); // 讀取輸入

            switch (input) {
                case "1":
                    playCraps(); // [cite: 46] 呼叫擲骰子
                    break;
                case "2":
                    playBullsAndCows(); // [cite: 47] 呼叫幾A幾B
                    break;
                case "3":
                    playRPS(); // [cite: 48] 呼叫自行開發遊戲
                    break;
                case "4":
                    playTicTacToe(); // 呼叫井字遊戲
                    break;
                case "5":
                    System.out.println("系統結束，拜拜！"); // [cite: 50]
                    System.exit(0);
                default:
                    System.out.println("輸入錯誤，請重新輸入。");
            }
            System.out.println(); // 空一行排版
        }
    }

    // ==========================================
    // 遊戲 1: 擲骰子 (Craps)
    // 規則來源: [cite: 8-12]
    // ==========================================
    public static void playCraps() {
        System.out.println("\n--- 遊戲開始: 擲骰子 (Craps) ---");

        // 第一輪擲骰子
        int sum = rollDice();
        System.out.println("你擲出的總和是: " + sum);

        // [cite: 10] 判定規則
        if (sum == 7 || sum == 11) {
            System.out.println("結果: 恭喜！你直接獲勝 (Natural)！");
        } else if (sum == 2 || sum == 3 || sum == 12) {
            System.out.println("結果: 糟糕！你輸了 (Craps)！"); // 莊家獲勝
        } else {
            // [cite: 10] 建立 "點數" (Point)
            int point = sum;
            System.out.println("你的點數 (Point) 是: " + point);
            System.out.println("請繼續擲骰子，直到擲出 " + point + " (贏) 或 7 (輸)...");

            boolean keepRolling = true;
            while (keepRolling) {
                int newSum = rollDice();
                System.out.println(" -> 玩家擲出: " + newSum);

                if (newSum == point) {
                    System.out.println("結果: 恭喜！你擲出了點數，你贏了！");
                    keepRolling = false;
                } else if (newSum == 7) {
                    System.out.println("結果: 慘了！你擲出了 7，你輸了！");
                    keepRolling = false;
                }
                // 如果不是 Point 也不是 7，就繼續迴圈 (繼續擲)
            }
        }
    }

    // 擲骰子的小工具：產生 2-12 的隨機數字
    public static int rollDice() {
        // [cite: 10] 兩顆骰子，每顆 1-6
        int die1 = (int) (Math.random() * 6) + 1;
        int die2 = (int) (Math.random() * 6) + 1;
        return die1 + die2;
    }

    // ==========================================
    // 遊戲 2: 幾A幾B (Bulls and Cows)
    // 規則來源: [cite: 25-26]
    // ==========================================
    public static void playBullsAndCows() {
        System.out.println("\n--- 遊戲開始: 猜數字 (1A2B) ---");

        // 1. 電腦產生一組不重複的四位數
        String secret = generateSecretNumber();
        // 測試時若想偷看答案，可以把下面這行註解打開
        // System.out.println("(測試用) 答案是: " + secret);

        int a = 0, b = 0;
        int count = 0;

        // 2. 玩家開始猜，直到 4A0B
        while (a != 4) {
            System.out.print("請輸入 4 個不重複的數字 (例如 1234): ");
            String guess = scanner.next();

            // 若輸入 5 則跳出遊戲
            if (guess.equals("5")) {
                System.out.println("已中斷遊戲，返回主選單。");
                return;
            }

            // 基本防呆：檢查長度
            if (guess.length() != 4) {
                System.out.println("格式錯誤！請輸入剛好 4 位數。");
                continue;
            }

            // 計算 A 和 B
            a = 0;
            b = 0;
            for (int i = 0; i < 4; i++) {
                char c = guess.charAt(i); // 玩家猜的第 i 個字元

                // 檢查是否位置正確 (A)
                if (c == secret.charAt(i)) {
                    a++;
                }
                // 檢查是否包含該數字但位置不對 (B)
                else if (secret.indexOf(c) != -1) {
                    b++;
                }
            }

            count++;
            System.out.println("結果: " + a + "A" + b + "B"); // [cite: 26]
        }
        System.out.println("恭喜！你猜對了！答案就是 " + secret + "，共猜了 " + count + " 次。");
    }

    // 產生 4 位不重複數字的邏輯
    public static String generateSecretNumber() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers); // 洗牌，打亂數字

        String result = "";
        for (int i = 0; i < 4; i++) {
            result += numbers.get(i); // 取前 4 個
        }
        return result;
    }

    // ==========================================
    // 遊戲 3: 剪刀石頭布 (Rock-Paper-Scissors)
    // 這是自行開發的第三個遊戲
    // ==========================================
    public static void playRPS() {
        System.out.println("\n--- 遊戲開始: 剪刀石頭布 ---");
        System.out.println("請出拳： 1.剪刀  2.石頭  3.布");

        int userChoice;
        try {
            userChoice = Integer.parseInt(scanner.next());
        } catch (Exception e) {
            System.out.println("輸入錯誤，當作你棄權輸了！");
            return;
        }

        if (userChoice < 1 || userChoice > 3) {
            System.out.println("輸入無效 (請輸入 1-3)");
            return;
        }

        // 電腦隨機出拳 (1-3)
        int pcChoice = (int) (Math.random() * 3) + 1;

        // 顯示出拳結果
        System.out.println("你出: " + getRPSName(userChoice));
        System.out.println("電腦: " + getRPSName(pcChoice));

        // 判斷輸贏
        if (userChoice == pcChoice) {
            System.out.println("結果: 平手！");
        } else if ((userChoice == 1 && pcChoice == 3) ||
                (userChoice == 2 && pcChoice == 1) ||
                (userChoice == 3 && pcChoice == 2)) {
            // 剪刀贏布(1vs3), 石頭贏剪刀(2vs1), 布贏石頭(3vs2)
            System.out.println("結果: 你贏了！");
        } else {
            System.out.println("結果: 你輸了，電腦獲勝！");
        }
    }

    // ==========================================
    // 遊戲 4: 井字遊戲 (Tic Tac Toe)
    // ==========================================
    public static void playTicTacToe() {
        System.out.println("\n--- 遊戲開始: 井字遊戲 ---");
        char[][] board = {
                { ' ', ' ', ' ' },
                { ' ', ' ', ' ' },
                { ' ', ' ', ' ' }
        };

        // 玩家執 'X', 電腦執 'O'
        boolean playerTurn = true;
        boolean gameEnded = false;

        while (!gameEnded) {
            printBoard(board);
            if (playerTurn) {
                System.out.print("換你下了 (輸入 1-9 對應位置, 例如左上是1, 右下是9): ");
                String input = scanner.next();

                // 離開機制
                if (input.equals("q") || input.equals("quit")) {
                    System.out.println("遊戲中止。");
                    return;
                }

                int pos;
                try {
                    pos = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("請輸入有效數字！");
                    continue;
                }

                if (pos < 1 || pos > 9) {
                    System.out.println("輸入無效 (1-9)！");
                    continue;
                }

                int row = (pos - 1) / 3;
                int col = (pos - 1) % 3;

                if (board[row][col] != ' ') {
                    System.out.println("該位置已經有人了！");
                    continue;
                }

                board[row][col] = 'X';

            } else {
                // 電腦回合 (隨機下)
                System.out.println("電腦思考中...");
                boolean validMove = false;
                while (!validMove) {
                    int r = (int) (Math.random() * 3);
                    int c = (int) (Math.random() * 3);
                    if (board[r][c] == ' ') {
                        board[r][c] = 'O';
                        validMove = true;
                    }
                }
            }

            // 檢查輸贏
            if (checkWin(board, 'X')) {
                printBoard(board);
                System.out.println("恭喜！你贏了！");
                gameEnded = true;
            } else if (checkWin(board, 'O')) {
                printBoard(board);
                System.out.println("電腦贏了，再接再厲。");
                gameEnded = true;
            } else if (isBoardFull(board)) {
                printBoard(board);
                System.out.println("平手 (Draw)！");
                gameEnded = true;
            }

            // 換手
            playerTurn = !playerTurn;
        }
    }

    private static void printBoard(char[][] board) {
        System.out.println("-------------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " | ");
            }
            System.out.println("\n-------------");
        }
    }

    private static boolean checkWin(char[][] board, char symbol) {
        // 檢查列
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol)
                return true;
        }
        // 檢查行
        for (int j = 0; j < 3; j++) {
            if (board[0][j] == symbol && board[1][j] == symbol && board[2][j] == symbol)
                return true;
        }
        // 檢查對角線
        if (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol)
            return true;
        if (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol)
            return true;

        return false;
    }

    private static boolean isBoardFull(char[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ')
                    return false;
            }
        }
        return true;
    }

    // 把數字轉成文字的小工具
    public static String getRPSName(int i) {
        switch (i) {
            case 1:
                return "剪刀";
            case 2:
                return "石頭";
            case 3:
                return "布";
            default:
                return "未知";
        }
    }
}
