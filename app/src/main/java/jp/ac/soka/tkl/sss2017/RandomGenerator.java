package jp.ac.soka.tkl.sss2017;

/*------------------------------*
 *  RandomGenerator.java        *
 *  乱数生成                    *
 *  last update : July 20, 2017 *
 *------------------------------*/

import java.security.SecureRandom;

class RandomGenerator {

    RandomGenerator() {}

    static int rand(int num) {
        SecureRandom random = new SecureRandom();
        return random.nextInt(num);
    }

}
