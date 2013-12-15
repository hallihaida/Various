import java.util.HashMap;


public class MathProblem
{

	private class Answer
	{
		public int answer;
		public String eqn;
		
		Answer()
		{
			answer = 0;
			eqn = null;
		}
		
		public String toString()
		{
			return eqn;
		}
	}
	
	private int limt;
	private int [] q_digits;
	private int [] digits;
	HashMap<Integer, String> answers;
		
	MathProblem(int question, int limit)
	{
		this.limt = limit;
		int digit_count = 1;
		int temp = question/10;
		while (temp > 0)
		{
			digit_count++;
			temp = temp / 10;
		}
		
		q_digits = new int[digit_count];
		temp = question;
		for (int i = digit_count-1; i >= 0; i--)
		{
			q_digits[i] = temp % 10;
			temp = temp/10;
		}

		this.digits = null;
		this.answers = new HashMap<Integer, String>();
	}

	public void solveAll()
	{
		// Do the simple one digit combinations
		q_digits.clone();
		int digi_pos[] = new int[q_digits.length];
		solveVariations(q_digits, 0, digi_pos);
		
		// Now try combinations of multiple digits
		for (int i = 2; i < q_digits.length; i++)
		{
			solveDigitCombo(0, i, digi_pos);
		}
	}
	
	
	public void solveDigitCombo(int idx, int max_digits, int digi_pos[])
	{
		if (idx == q_digits.length)
		{
			int max_numbers = q_digits.length/max_digits;
			
			for (int k = 1; (k <= max_numbers); k++)
			{
				int [] qm_digits = new int[k + (q_digits.length - (k *max_digits))];
				boolean zero_prefix = false;
				int qd_iter = 0;
				int qmd_iter = 0;
				int max_num_cur = k;
				while(max_num_cur-- > 0)
				{
					int num = q_digits[digi_pos[qd_iter++]];
					if ((num == 0) && (max_digits > 1))
					{
						zero_prefix = true;
						break;
					}
					for (int j = max_digits - 1; j > 0; j--)
						num = num * 10 + q_digits[digi_pos[qd_iter++]];
					qm_digits[qmd_iter++] = num;				
				}
				
				if (zero_prefix)
					continue;
				
				while((qmd_iter < qm_digits.length) && (qd_iter < q_digits.length))
					qm_digits[qmd_iter++] = q_digits[digi_pos[qd_iter++]];
				
				int d_pos[] = new int[qm_digits.length];
				solveVariations(qm_digits, 0, d_pos);
			}
			return;
		}
		
		for (int i = 0; i < q_digits.length; i++)
		{
			int j;
			for (j = 0; (j < idx) && (digi_pos[j] != i) ; j++);
			if (j == idx)
			{
				digi_pos[idx] = i;
				solveDigitCombo(idx+1, max_digits, digi_pos);
			}
		}		
	}

	public void solveVariations(int[] q_digits, int idx, int digi_pos[])
	{
		if (idx == q_digits.length)
		{
			digits = new int[q_digits.length];
			int qd_iter = 0;
			int d_iter = 0;
			while((d_iter < digits.length) && (qd_iter < q_digits.length))
				digits[d_iter++] = q_digits[digi_pos[qd_iter++]];

//			System.out.print(">>>>>>>>>>>> Numbers: [");
//			for (int i : digits )
//				System.out.printf("%d,", i);
//			System.out.println("]");

			solve(null, 0);
			return;
		}
		else
		{
			for (int i = 0; i < q_digits.length; i++)
			{
				int j;
				for (j = 0; (j < idx) && (digi_pos[j] != i) ; j++);
				if (j == idx)
				{
					digi_pos[idx] = i;
					solveVariations(q_digits, idx+1, digi_pos);
				}
			}
		}
	}
	
	public void solve(Answer prev_ans, int idx)
	{
		if (idx >= digits.length)
		{
			if ((prev_ans != null) && (prev_ans.answer > 0) && (prev_ans.answer <= this.limt))
			{
				String hsa = answers.get(prev_ans.answer);
				if (hsa == null)
					answers.put((int)prev_ans.answer, prev_ans.eqn);
			}
			return;
		}
		
		int a, b;
		String a_str, b_str;
		
		if (prev_ans != null)
		{
			//System.out.printf("==>[P:%d,", prev_ans.answer);
			a = prev_ans.answer;
			a_str = prev_ans.eqn;
		}
		else
		{
			//System.out.printf("==>[N:%d,", digits[idx]);
			a = digits[idx++];
			a_str = Integer.toString(a);
		}
		
		if (idx < digits.length)
		{
			//System.out.printf("N:%d]\n", digits[idx]);
			b = digits[idx++];
			b_str = Integer.toString(b);
			doOp(a, a_str, b, b_str, idx);
		}
		else
		{
			//System.out.printf("()]\n");
			Answer ans = new Answer();
			ans.answer = a;
			ans.eqn = a_str;
			solve(ans, idx);
		}
		
		return;
	}
	

	private void doOp(int a, String a_str, int b, String b_str, int idx)
	{
		Answer ans = new Answer();
		int b_fact = factorial(b);
		int a_fact = factorial(a);
		String a_fact_str = "(" + a_str + ")!";
		String b_fact_str = "(" + b_str + ")!";
		
		ans.answer = a+b;
		ans.eqn = a_str + "+" + b_str;
		solve(ans, idx);
		
		ans.answer = a_fact+ b_fact;
		ans.eqn = a_fact_str + " + " + b_fact_str;
		solve(ans, idx);
		
		ans.answer = a_fact+ b;
		ans.eqn = a_fact_str + " + " + b_str;
		solve(ans, idx);
		
		ans.answer = a + b_fact;
		ans.eqn = a_str + " + " + b_fact_str;
		solve(ans, idx);
		
		ans.answer = factorial(a+b);
		ans.eqn = "(" + a_str + "+" + b_str + ")!";
		solve(ans, idx);
		
		ans.answer = a - b;
		ans.eqn = "(" + a_str + ") - (" + b_str +")";
		solve(ans, idx);

		ans.answer = a_fact - b_fact;
		ans.eqn = a_fact_str + " - " + b_fact_str;
		solve(ans, idx);
		
		ans.answer = a_fact - b;
		ans.eqn = a_fact_str + " - (" + b_str + ")";
		solve(ans, idx);
		
		ans.answer = a - b_fact;
		ans.eqn = "(" + a_str + ") - " + b_fact_str;
		solve(ans, idx);
		
		ans.answer = factorial(a-b);
		ans.eqn = "((" + a_str + ") - (" + b_str + "))!";
		solve(ans, idx);
		
		ans.answer = b - a;
		ans.eqn = "(" + b_str + ") - (" + a_str + ")";
		solve(ans, idx);
		
		ans.answer = b_fact - a;
		ans.eqn = b_fact_str + " - (" + a_str + ")";
		solve(ans, idx);
		
		ans.answer = b - a_fact;
		ans.eqn = "(" + b_str + ") - " + a_fact_str;
		solve(ans, idx);
		
		ans.answer = b_fact - a_fact;
		ans.eqn = b_fact_str + " - " + a_fact_str;
		solve(ans, idx);
		
		ans.answer = factorial(b-a);
		ans.eqn = "((" + b_str + ") - (" + a_str + "))!";
		solve(ans, idx);
		
		
		ans.answer = a * b;
		ans.eqn = "(" + a_str + ") * (" + b_str + ")";
		solve(ans, idx);
		
		ans.answer = a_fact * b;
		ans.eqn = a_fact_str + " * (" + b_str + ")";
		solve(ans, idx);
		
		ans.answer = a * b_fact;
		ans.eqn = "("+ a_str + ") * " + b_fact_str;
		solve(ans, idx);
		
		ans.answer = a_fact * b_fact;
		ans.eqn = a_fact_str + " * " + b_fact_str;
		solve(ans, idx);
		
		ans.answer = factorial(a*b);
		ans.eqn = "(" + a_str + "*" + b_str + ")!";
		solve(ans, idx);
		
		
		if ((b != 0) && ((a % b) == 0))
		{
			ans.answer = a / b;
			ans.eqn = "(" + a_str + ") / (" + b_str + ")";
			solve(ans, idx);
			
			ans.answer = factorial(a/b);
			ans.eqn = "((" + a_str + ")/(" + b_str + "))!";
			solve(ans, idx);
			
		}
		
		if ((b_fact != 0) && ((a % b_fact) == 0))
		{
			ans.answer = a / b_fact;
			ans.eqn = "(" + a_str + ") / " + b_fact_str;
			solve(ans, idx);
		}
		
		if ((b != 0) && ((a_fact % b) == 0))
		{
			ans.answer = a_fact / b;
			ans.eqn = a_fact_str + " / (" + b_str + ")";
			solve(ans, idx);
		}
		
		if ((b_fact != 0) && ((a_fact % b_fact) == 0))
		{
			ans.answer = a_fact / b_fact;
			ans.eqn = a_fact_str + " / " + b_fact_str;
			solve(ans, idx);
		}
		
		if ((a != 0) && ((b % a) == 0))
		{
			ans.answer = b / a;
			ans.eqn = "(" + b_str + ") / (" + a_str + ")";
			solve(ans, idx);
			
			ans.answer = factorial(b/a);
			ans.eqn = "((" + b_str + ") / (" + a_str + "))!";
			solve(ans, idx);
		}
		
		if ((a_fact != 0) && ((b % a_fact) == 0))
		{
			ans.answer = b / a_fact;
			ans.eqn = "(" + b_str + ") / " + a_fact_str;
			solve(ans, idx);
		}

		if ((a != 0) && ((b_fact % a) == 0))
		{
			ans.answer = b_fact / a;
			ans.eqn = b_fact_str + " / (" + a_str + ")";
			solve(ans, idx);
		}
		
		if ((a_fact != 0) && ((b_fact % a_fact) == 0))
		{
			ans.answer = b_fact / a_fact;
			ans.eqn = b_fact_str + " / " + a_fact_str;
			solve(ans, idx);
		}
		
		ans.answer = (int)Math.pow(a, b);
		ans.eqn = "(" + a_str + ") ^ (" + b_str + ")";
		solve(ans, idx);
		
		ans.answer = (int)Math.pow(b, a);
		ans.eqn = "(" + b_str + ") ^ (" + a_str + ")";
		solve(ans, idx);
		
		ans.answer = (int)Math.pow(a_fact, b);
		ans.eqn = "(" + a_fact_str + ") ^ (" + b_str + ")";
		solve(ans, idx);
		
		ans.answer = (int)Math.pow(b_fact, a);
		ans.eqn = "(" + b_fact_str + ") ^ (" + a_str + ")";
		solve(ans, idx);
		
		ans.answer = (int)Math.pow(a_fact, b_fact);
		ans.eqn = "(" + a_fact_str + ") ^ (" + b_fact_str + ")";
		solve(ans, idx);
		
		ans.answer = (int)Math.pow(b_fact, a_fact);
		ans.eqn = "(" + b_fact_str + ") ^ (" + a_fact_str + ")";
		solve(ans, idx);
	}
	
	public void printSolutions()
	{
		for (int i = 1; i <= this.limt; i++)
		{
			String eqn = answers.get(i);
			if ((eqn != null) && (!eqn.isEmpty()))
				System.out.printf("%d: %s\n", i, eqn);
			else
				System.out.printf("%d: -\n", i);
		}			
	}
		
	private int factorial(int i)
	{
		if ((i < 0) || (i > 100))
			return 0;
		if (i == 0)
			return 1;
		int ret = 1;
		for (int j = i; j > 1; j--)
			ret *= j;
		return ret;
	}
	
	public static void main(String[] args)
	{
		int q, limit;
		
		if (args.length > 0)
			q = Integer.valueOf(args[0]);
		else
			q = 2013;
		
		if (args.length > 1)
			limit = Integer.valueOf(args[1]);
		else
			limit = 100;
		
		MathProblem p = new MathProblem(q, limit);
		p.solveAll();
		p.printSolutions();
	}
}
