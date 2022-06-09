package duel.threads.task.impl;

import duel.threads.task.BasicTask;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.function.Supplier;

public class ChallengeThreeTask extends BasicTask implements Supplier<ChallengeThreeTask.PiCalculation> {

    private final int precision;

    private BigDecimal pi = new BigDecimal("3.14");

    public static void main(String[] args) {
        new ChallengeThreeTask(1, Integer.parseInt(args[0])).run();
    }

    public ChallengeThreeTask(int taskNumber, int precision) {
        super(taskNumber);
        this.precision = precision;
    }

    private static BigDecimal factorial(final BigDecimal n) {
        var result = BigDecimal.ONE;
        for (var i = BigDecimal.ONE; i.compareTo(n) < 0; i = i.add(BigDecimal.ONE)) {
            result = result.multiply(i);
        }
        return result;
    }

    public BigDecimal getPi() {
        return pi;
    }

    @Override
    public PiCalculation get() {
        calculatePi();
        return new PiCalculation(this.precision, this.pi);
    }

    @Override
    public void run() {
        calculatePi();
    }

    private void calculatePi() {
        final var precision = new BigDecimal(this.precision);
        final var context = new MathContext(this.precision * 2, RoundingMode.HALF_UP); // TODO play with this number to increase final accuracy. Compounded rounding over 10's of thousands of digits leads to some weird results
        final var numberOfIterations = precision.divide(new BigDecimal("14"), RoundingMode.CEILING);
        final var constantTerm = new BigDecimal("426880").multiply(new BigDecimal("10005").sqrt(context));
        var multinomialTerm = BigDecimal.ONE;
        var exponentialTerm = BigDecimal.ONE;
        final var exponentialTermConstant = new BigDecimal("-262537412640768000");
        var linearTerm = new BigDecimal("13591409");
        final var linearTermConstant = new BigDecimal("545140134");
        var partialSum = new BigDecimal("13591409");

        for (BigDecimal k = BigDecimal.ZERO; k.compareTo(numberOfIterations) < 0; k = k.add(BigDecimal.ONE)) {
            final var k_6 = new BigDecimal("6").multiply(k);
            final var k_3 = new BigDecimal("3").multiply(k);
            multinomialTerm = factorial(k_6)
                    .divide(
                            (factorial(k_3).multiply(factorial(k).pow(3, context))
                            ), context);
            linearTerm = linearTerm.add(linearTermConstant);
            exponentialTerm = exponentialTerm.multiply(exponentialTermConstant);
            partialSum = partialSum.add(multinomialTerm
                    .multiply(linearTerm)
                    .divide(exponentialTerm, context));
        }
        pi =  constantTerm.divide(partialSum, context).setScale(this.precision, RoundingMode.HALF_UP);
    }

    public record PiCalculation(int precision, BigDecimal pi) {};

}
