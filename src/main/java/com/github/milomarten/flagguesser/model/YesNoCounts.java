package com.github.milomarten.flagguesser.model;

import com.github.milomarten.flagguesser.model.grid.YesCounts;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class YesNoCounts {
    private int present;
    private int absent;
    private boolean allFound;

    public YesCounts toYesCounts() {
        return new YesCounts(this.present, this.allFound);
    }
}
