# AwesomePasswordChecker

The `AwesomePasswordChecker` is a Java library designed to evaluate the strength of passwords based on a predefined set of cluster centers. It includes utilities to compute MD5 hashes, transform passwords into feature masks, and calculate their distance from password clusters. This tool is useful for analyzing password complexity and comparing them against security benchmarks.

## Features

- **Password Strength Analysis**: Evaluate password strength using cluster-based methods.
- **Customizable Clusters**: Load custom cluster centers from a CSV file.
- **MD5 Hash Computation**: Compute MD5 hashes for strings manually.
- **Mask-Based Analysis**: Transform passwords into feature masks for comparison.

## Prerequisites

- Java 8 or higher
- A CSV

## Installation

1) Clone the repository:
```bash
git clone https://github.com/mafortias/code_review_tp1.git
cd AwesomePasswordChecker
```
2) Compile the code 
```bash
javac fr/isima/zz2/f5/ai/checkpassword/AwesomePasswordChecker.java
```

## Structure

- `cluster_centers_HAC_aff.csv` : Default file containing cluster centers used for password strength analysis.
- `AwesomePasswordChecker` : Main class implementing password analysis and MD5 computation.

## Usage 

### Analyse Password Strength

```java
import fr.isima.zz2.f5.ai.checkpassword.AwesomePasswordChecker;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        try {
            AwesomePasswordChecker checker = AwesomePasswordChecker.getInstance(new File("path/to/cluster_centers.csv"));
            String password = "examplePassword";
            double distance = checker.getDistance(password);
            System.out.println("Password Strength Distance: " + distance);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
```

### Compute MD5 Hash

```java
import fr.isima.zz2.f5.ai.checkpassword.AwesomePasswordChecker;

public class Main {
    public static void main(String[] args) {
        String hash = AwesomePasswordChecker.computeMd5("examplePassword");
        System.out.println("MD5 Hash: " + hash);
    }
}
```

### Run Main Method

You can also run the main method included in the AwesomePasswordChecker class to see examples of both features in action:
```bash
java fr.isima.zz2.f5.ai.checkpassword.AwesomePasswordChecker
```

### Notes

1) The cluster center file (`.csv`) should have one center per line, with values separated by commas.
2) If no custom file is provided, the class attempts to load `cluster_centers_HAC_aff.csv` from the classpath.

## License

This project is licensed under the Attribution-NonCommercial 4.0 International. See `LICENSE` for details.
