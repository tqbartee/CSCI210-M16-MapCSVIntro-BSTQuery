import com.zybooks.dsaj.treebook.LinkedBinaryTree;
import com.zybooks.dsaj.treebook.Position;
import com.zybooks.dsaj.util.Entry;
import edu.desu.utilities.UtilityMethodsBinaryTreeBook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwingBinaryTreeDisplay {

    static class BinaryTreePanel<E> extends JPanel {
        private static final int MAX_LEVELS = 5;
        private final LinkedBinaryTree<E> tree;
        private final int nodeWidth = 120;
        private final int nodeHeight = 42;
        private final int vGap = 65;
        private final int minNodeDistance = 90;
        private final int marginLeft = 20;
        private final int marginTop = 20;
        private final int maxContentChars = 15;

        public BinaryTreePanel(LinkedBinaryTree<E> tree) {
            this.tree = tree;
            setBackground(Color.WHITE);
            ToolTipManager.sharedInstance().registerComponent(this);
        }

        private int getTreeHeight(Position<E> p, int level) {
            if (p == null || level >= MAX_LEVELS) return 0;
            int leftH = getTreeHeight(tree.left(p), level + 1);
            int rightH = getTreeHeight(tree.right(p), level + 1);
            return 1 + Math.max(leftH, rightH);
        }

        private void computeInOrderIndices(Position<E> p, Map<Position<E>, Integer> map, int[] counter, int level) {
            if (p == null || level >= MAX_LEVELS) return;
            computeInOrderIndices(tree.left(p), map, counter, level + 1);
            map.put(p, counter[0]++);
            computeInOrderIndices(tree.right(p), map, counter, level + 1);
        }

        @Override
        public Dimension getPreferredSize() {
            if (tree.root() == null) {
                return new Dimension(800, 500);
            }
            int h = getTreeHeight(tree.root(), 0);
            Map<Position<E>, Integer> xMap = new HashMap<>();
            computeInOrderIndices(tree.root(), xMap, new int[]{0}, 0);
            int nodeCount = xMap.size();

            int preferredWidth = Math.max(800, marginLeft + nodeCount * minNodeDistance + marginLeft + 40);
            int preferredHeight = Math.max(500, marginTop + Math.max(1, h) * vGap + marginTop + nodeHeight + 15);

            return new Dimension(preferredWidth, preferredHeight);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (tree.root() != null) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                Map<Position<E>, Integer> xMap = new HashMap<>();
                computeInOrderIndices(tree.root(), xMap, new int[]{0}, 0);
                drawTree(g2, tree.root(), xMap, 0);
            }
        }

        private List<String> getNodeLines(E element) {
            List<String> lines = new ArrayList<>();
            if (element == null) {
                lines.add("null");
                return lines;
            }

            if (element instanceof Entry) {
                Entry<?, ?> entry = (Entry<?, ?>) element;
                Object k = entry.getKey();
                Object v = entry.getValue();

                String kStr = (k != null) ? k.toString().trim() : "null";
                lines.add("K: " + truncate(kStr, maxContentChars));

                if (v != null) {
                    String vStr = v.toString().trim();
                    String firstVLine = vStr.split("\n")[0].trim();
                    lines.add("V: " + truncate(firstVLine, maxContentChars));
                }
            } else {
                String str = element.toString().trim();
                if (str.contains("=") && !str.contains("\n")) {
                    String[] parts = str.split("=", 2);
                    lines.add("K: " + truncate(parts[0].trim(), maxContentChars));
                    lines.add("V: " + truncate(parts[1].trim(), maxContentChars));
                } else {
                    String[] parts = str.split("\n");
                    for (int i = 0; i < Math.min(parts.length, 2); i++) {
                        lines.add(truncate(parts[i].trim(), maxContentChars));
                    }
                }
            }
            return lines;
        }

        private String truncate(String text, int maxLen) {
            if (text == null) return "";
            if (text.length() <= maxLen) return text;
            return text.substring(0, Math.max(0, maxLen - 2)) + "..";
        }

        private void drawTree(Graphics2D g2, Position<E> p, Map<Position<E>, Integer> xMap, int level) {
            if (p == null || level >= MAX_LEVELS) {
                return;
            }

            Integer index = xMap.get(p);
            if (index == null) {
                return;
            }
            int x = marginLeft + index * minNodeDistance + nodeWidth / 2;
            int y = marginTop + level * vGap + nodeHeight / 2;

            Position<E> leftP = tree.left(p);
            if (leftP != null && level + 1 < MAX_LEVELS) {
                Integer leftIndex = xMap.get(leftP);
                if (leftIndex != null) {
                    int childX = marginLeft + leftIndex * minNodeDistance + nodeWidth / 2;
                    int childY = marginTop + (level + 1) * vGap + nodeHeight / 2;
                    g2.setColor(new Color(130, 145, 165));
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawLine(x, y + nodeHeight / 2, childX, childY - nodeHeight / 2);
                    drawTree(g2, leftP, xMap, level + 1);
                }
            }

            Position<E> rightP = tree.right(p);
            if (rightP != null && level + 1 < MAX_LEVELS) {
                Integer rightIndex = xMap.get(rightP);
                if (rightIndex != null) {
                    int childX = marginLeft + rightIndex * minNodeDistance + nodeWidth / 2;
                    int childY = marginTop + (level + 1) * vGap + nodeHeight / 2;
                    g2.setColor(new Color(130, 145, 165));
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawLine(x, y + nodeHeight / 2, childX, childY - nodeHeight / 2);
                    drawTree(g2, rightP, xMap, level + 1);
                }
            }

            // Draw Node Box
            int boxX = x - nodeWidth / 2;
            int boxY = y - nodeHeight / 2;

            g2.setColor(new Color(248, 250, 254));
            g2.fillRoundRect(boxX, boxY, nodeWidth, nodeHeight, 8, 8);

            g2.setColor(new Color(70, 95, 140));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(boxX, boxY, nodeWidth, nodeHeight, 8, 8);

            // Draw lines of text
            List<String> lines = getNodeLines(p.getElement());
            if (lines.size() == 1) {
                g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
                FontMetrics fm = g2.getFontMetrics();
                g2.setColor(new Color(25, 35, 55));
                String line = lines.get(0);
                g2.drawString(line, x - fm.stringWidth(line) / 2, y + fm.getAscent() / 2 - 2);
            } else if (lines.size() >= 2) {
                g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                FontMetrics fmKey = g2.getFontMetrics();
                g2.setColor(new Color(20, 35, 60));
                String line1 = lines.get(0);
                g2.drawString(line1, x - fmKey.stringWidth(line1) / 2, y - 2);

                g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                FontMetrics fmVal = g2.getFontMetrics();
                g2.setColor(new Color(75, 85, 100));
                String line2 = lines.get(1);
                g2.drawString(line2, x - fmVal.stringWidth(line2) / 2, y + fmVal.getAscent());
            }

            // If this node is at level 4 (5th level) and has children in the tree, show a subtle indicator
            if (level + 1 == MAX_LEVELS && (tree.left(p) != null || tree.right(p) != null)) {
                g2.setFont(new Font("SansSerif", Font.BOLD, 9));
                FontMetrics fmDots = g2.getFontMetrics();
                String more = "▼";
                g2.setColor(new Color(110, 130, 160));
                g2.drawString(more, x - fmDots.stringWidth(more) / 2, boxY + nodeHeight + 11);
            }
        }

        @Override
        public String getToolTipText(MouseEvent event) {
            if (tree.root() == null) return null;
            Map<Position<E>, Integer> xMap = new HashMap<>();
            computeInOrderIndices(tree.root(), xMap, new int[]{0}, 0);
            Point pt = event.getPoint();
            return findToolTip(tree.root(), xMap, 0, pt);
        }

        private String findToolTip(Position<E> p, Map<Position<E>, Integer> xMap, int level, Point pt) {
            if (p == null || level >= MAX_LEVELS) return null;
            Integer index = xMap.get(p);
            if (index == null) return null;
            int x = marginLeft + index * minNodeDistance + nodeWidth / 2;
            int y = marginTop + level * vGap + nodeHeight / 2;
            Rectangle rect = new Rectangle(x - nodeWidth / 2, y - nodeHeight / 2, nodeWidth, nodeHeight);
            if (rect.contains(pt)) {
                return formatFullToolTip(p, level);
            }
            String leftTip = findToolTip(tree.left(p), xMap, level + 1, pt);
            if (leftTip != null) return leftTip;
            return findToolTip(tree.right(p), xMap, level + 1, pt);
        }

        private String formatFullToolTip(Position<E> p, int level) {
            if (p == null || p.getElement() == null) return "null";
            E element = p.getElement();
            boolean hasHiddenChildren = (level + 1 == MAX_LEVELS) && (tree.left(p) != null || tree.right(p) != null);
            String extra = hasHiddenChildren ? "<br><i style='color:gray;'>(Subtree extends beyond 5 levels)</i>" : "";

            if (element instanceof Entry) {
                Entry<?, ?> entry = (Entry<?, ?>) element;
                String k = String.valueOf(entry.getKey());
                String v = String.valueOf(entry.getValue());
                return "<html><b>Key:</b> " + escapeHtml(k) + "<br><b>Value:</b> " + escapeHtml(v).replace("\n", "<br>&nbsp;&nbsp;") + extra + "</html>";
            }
            return "<html>" + escapeHtml(element.toString()).replace("\n", "<br>") + extra + "</html>";
        }

        private String escapeHtml(String text) {
            if (text == null) return "";
            return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        }
    }

    public static <E> void showInWindow(LinkedBinaryTree<E> T) {
        JFrame frame = new JFrame("LinkedBinaryTree Visualization");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 650);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        BinaryTreePanel<E> panel = new BinaryTreePanel<>(T);
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(25);
        scrollPane.getVerticalScrollBar().setUnitIncrement(25);

        JButton rebalanceButton = new JButton("Rebalance Tree");
        rebalanceButton.addActionListener(e -> {
            if (T != null) {
                if (T instanceof BinarySearchTree) {
                    ((BinarySearchTree<?, ?>) T).rebalanceTreeL109();
                } else {
                    java.util.ArrayList<E> list = UtilityMethodsBinaryTreeBook.generateArrayListFromBinaryTree(T);
                    T.root = buildBalancedSubtree(list, 0, list.size() - 1, null);
                }
                panel.revalidate();
                panel.repaint();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel infoLabel = new JLabel("Displaying top 5 levels from root  ");
        infoLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        infoLabel.setForeground(new Color(110, 120, 135));
        buttonPanel.add(infoLabel);
        buttonPanel.add(rebalanceButton);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private static <E> LinkedBinaryTree.Node<E> buildBalancedSubtree(java.util.List<E> list, int left, int right, LinkedBinaryTree.Node<E> parent) {
        if (left > right) {
            return null;
        }
        int mid = left + (right - left) / 2;
        LinkedBinaryTree.Node<E> node = new LinkedBinaryTree.Node<>(list.get(mid), parent, null, null);
        node.setLeft(buildBalancedSubtree(list, left, mid - 1, node));
        node.setRight(buildBalancedSubtree(list, mid + 1, right, node));
        return node;
    }

    public static void main(String[] args) {
        BinarySearchTree<Integer, Integer> bst = new BinarySearchTree<>();
        BSTBuilder.buildUnbalancedBST(bst);
        showInWindow(bst);
    }
}

