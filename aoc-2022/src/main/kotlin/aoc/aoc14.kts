import aoc.AocParser.Companion.parselines
import aoc.AocRunner
import aoc.util.Coord
import aoc.print

val day = 14

val testInput = """
498,4 -> 498,6 -> 496,6
503,4 -> 502,4 -> 502,9 -> 494,9
""".parselines.map { it.split("->").map {
    it.trim().split(",").let {
        it[0].toInt() to it[1].toInt()
    }
}}

val input = """
502,19 -> 507,19
523,100 -> 523,104 -> 519,104 -> 519,111 -> 528,111 -> 528,104 -> 526,104 -> 526,100
488,59 -> 488,51 -> 488,59 -> 490,59 -> 490,52 -> 490,59 -> 492,59 -> 492,55 -> 492,59 -> 494,59 -> 494,52 -> 494,59 -> 496,59 -> 496,50 -> 496,59 -> 498,59 -> 498,58 -> 498,59 -> 500,59 -> 500,56 -> 500,59 -> 502,59 -> 502,56 -> 502,59 -> 504,59 -> 504,57 -> 504,59
507,117 -> 521,117 -> 521,116
517,34 -> 522,34
513,97 -> 513,89 -> 513,97 -> 515,97 -> 515,94 -> 515,97 -> 517,97 -> 517,94 -> 517,97 -> 519,97 -> 519,93 -> 519,97 -> 521,97 -> 521,88 -> 521,97 -> 523,97 -> 523,94 -> 523,97
513,97 -> 513,89 -> 513,97 -> 515,97 -> 515,94 -> 515,97 -> 517,97 -> 517,94 -> 517,97 -> 519,97 -> 519,93 -> 519,97 -> 521,97 -> 521,88 -> 521,97 -> 523,97 -> 523,94 -> 523,97
510,75 -> 510,78 -> 505,78 -> 505,84 -> 518,84 -> 518,78 -> 515,78 -> 515,75
503,34 -> 508,34
501,15 -> 506,15
523,136 -> 523,138 -> 518,138 -> 518,145 -> 535,145 -> 535,138 -> 528,138 -> 528,136
513,113 -> 513,114 -> 527,114
488,59 -> 488,51 -> 488,59 -> 490,59 -> 490,52 -> 490,59 -> 492,59 -> 492,55 -> 492,59 -> 494,59 -> 494,52 -> 494,59 -> 496,59 -> 496,50 -> 496,59 -> 498,59 -> 498,58 -> 498,59 -> 500,59 -> 500,56 -> 500,59 -> 502,59 -> 502,56 -> 502,59 -> 504,59 -> 504,57 -> 504,59
497,72 -> 497,63 -> 497,72 -> 499,72 -> 499,65 -> 499,72 -> 501,72 -> 501,71 -> 501,72 -> 503,72 -> 503,67 -> 503,72 -> 505,72 -> 505,63 -> 505,72 -> 507,72 -> 507,71 -> 507,72 -> 509,72 -> 509,70 -> 509,72 -> 511,72 -> 511,69 -> 511,72
497,72 -> 497,63 -> 497,72 -> 499,72 -> 499,65 -> 499,72 -> 501,72 -> 501,71 -> 501,72 -> 503,72 -> 503,67 -> 503,72 -> 505,72 -> 505,63 -> 505,72 -> 507,72 -> 507,71 -> 507,72 -> 509,72 -> 509,70 -> 509,72 -> 511,72 -> 511,69 -> 511,72
497,72 -> 497,63 -> 497,72 -> 499,72 -> 499,65 -> 499,72 -> 501,72 -> 501,71 -> 501,72 -> 503,72 -> 503,67 -> 503,72 -> 505,72 -> 505,63 -> 505,72 -> 507,72 -> 507,71 -> 507,72 -> 509,72 -> 509,70 -> 509,72 -> 511,72 -> 511,69 -> 511,72
497,72 -> 497,63 -> 497,72 -> 499,72 -> 499,65 -> 499,72 -> 501,72 -> 501,71 -> 501,72 -> 503,72 -> 503,67 -> 503,72 -> 505,72 -> 505,63 -> 505,72 -> 507,72 -> 507,71 -> 507,72 -> 509,72 -> 509,70 -> 509,72 -> 511,72 -> 511,69 -> 511,72
488,59 -> 488,51 -> 488,59 -> 490,59 -> 490,52 -> 490,59 -> 492,59 -> 492,55 -> 492,59 -> 494,59 -> 494,52 -> 494,59 -> 496,59 -> 496,50 -> 496,59 -> 498,59 -> 498,58 -> 498,59 -> 500,59 -> 500,56 -> 500,59 -> 502,59 -> 502,56 -> 502,59 -> 504,59 -> 504,57 -> 504,59
523,136 -> 523,138 -> 518,138 -> 518,145 -> 535,145 -> 535,138 -> 528,138 -> 528,136
530,150 -> 535,150
526,133 -> 526,131 -> 526,133 -> 528,133 -> 528,129 -> 528,133 -> 530,133 -> 530,129 -> 530,133
523,100 -> 523,104 -> 519,104 -> 519,111 -> 528,111 -> 528,104 -> 526,104 -> 526,100
488,59 -> 488,51 -> 488,59 -> 490,59 -> 490,52 -> 490,59 -> 492,59 -> 492,55 -> 492,59 -> 494,59 -> 494,52 -> 494,59 -> 496,59 -> 496,50 -> 496,59 -> 498,59 -> 498,58 -> 498,59 -> 500,59 -> 500,56 -> 500,59 -> 502,59 -> 502,56 -> 502,59 -> 504,59 -> 504,57 -> 504,59
510,34 -> 515,34
497,72 -> 497,63 -> 497,72 -> 499,72 -> 499,65 -> 499,72 -> 501,72 -> 501,71 -> 501,72 -> 503,72 -> 503,67 -> 503,72 -> 505,72 -> 505,63 -> 505,72 -> 507,72 -> 507,71 -> 507,72 -> 509,72 -> 509,70 -> 509,72 -> 511,72 -> 511,69 -> 511,72
510,75 -> 510,78 -> 505,78 -> 505,84 -> 518,84 -> 518,78 -> 515,78 -> 515,75
488,59 -> 488,51 -> 488,59 -> 490,59 -> 490,52 -> 490,59 -> 492,59 -> 492,55 -> 492,59 -> 494,59 -> 494,52 -> 494,59 -> 496,59 -> 496,50 -> 496,59 -> 498,59 -> 498,58 -> 498,59 -> 500,59 -> 500,56 -> 500,59 -> 502,59 -> 502,56 -> 502,59 -> 504,59 -> 504,57 -> 504,59
488,59 -> 488,51 -> 488,59 -> 490,59 -> 490,52 -> 490,59 -> 492,59 -> 492,55 -> 492,59 -> 494,59 -> 494,52 -> 494,59 -> 496,59 -> 496,50 -> 496,59 -> 498,59 -> 498,58 -> 498,59 -> 500,59 -> 500,56 -> 500,59 -> 502,59 -> 502,56 -> 502,59 -> 504,59 -> 504,57 -> 504,59
524,34 -> 529,34
498,46 -> 502,46
510,46 -> 514,46
509,19 -> 514,19
513,97 -> 513,89 -> 513,97 -> 515,97 -> 515,94 -> 515,97 -> 517,97 -> 517,94 -> 517,97 -> 519,97 -> 519,93 -> 519,97 -> 521,97 -> 521,88 -> 521,97 -> 523,97 -> 523,94 -> 523,97
526,133 -> 526,131 -> 526,133 -> 528,133 -> 528,129 -> 528,133 -> 530,133 -> 530,129 -> 530,133
497,13 -> 502,13
504,46 -> 508,46
488,59 -> 488,51 -> 488,59 -> 490,59 -> 490,52 -> 490,59 -> 492,59 -> 492,55 -> 492,59 -> 494,59 -> 494,52 -> 494,59 -> 496,59 -> 496,50 -> 496,59 -> 498,59 -> 498,58 -> 498,59 -> 500,59 -> 500,56 -> 500,59 -> 502,59 -> 502,56 -> 502,59 -> 504,59 -> 504,57 -> 504,59
545,155 -> 545,157 -> 541,157 -> 541,160 -> 556,160 -> 556,157 -> 550,157 -> 550,155
513,97 -> 513,89 -> 513,97 -> 515,97 -> 515,94 -> 515,97 -> 517,97 -> 517,94 -> 517,97 -> 519,97 -> 519,93 -> 519,97 -> 521,97 -> 521,88 -> 521,97 -> 523,97 -> 523,94 -> 523,97
510,75 -> 510,78 -> 505,78 -> 505,84 -> 518,84 -> 518,78 -> 515,78 -> 515,75
497,72 -> 497,63 -> 497,72 -> 499,72 -> 499,65 -> 499,72 -> 501,72 -> 501,71 -> 501,72 -> 503,72 -> 503,67 -> 503,72 -> 505,72 -> 505,63 -> 505,72 -> 507,72 -> 507,71 -> 507,72 -> 509,72 -> 509,70 -> 509,72 -> 511,72 -> 511,69 -> 511,72
513,97 -> 513,89 -> 513,97 -> 515,97 -> 515,94 -> 515,97 -> 517,97 -> 517,94 -> 517,97 -> 519,97 -> 519,93 -> 519,97 -> 521,97 -> 521,88 -> 521,97 -> 523,97 -> 523,94 -> 523,97
488,59 -> 488,51 -> 488,59 -> 490,59 -> 490,52 -> 490,59 -> 492,59 -> 492,55 -> 492,59 -> 494,59 -> 494,52 -> 494,59 -> 496,59 -> 496,50 -> 496,59 -> 498,59 -> 498,58 -> 498,59 -> 500,59 -> 500,56 -> 500,59 -> 502,59 -> 502,56 -> 502,59 -> 504,59 -> 504,57 -> 504,59
488,59 -> 488,51 -> 488,59 -> 490,59 -> 490,52 -> 490,59 -> 492,59 -> 492,55 -> 492,59 -> 494,59 -> 494,52 -> 494,59 -> 496,59 -> 496,50 -> 496,59 -> 498,59 -> 498,58 -> 498,59 -> 500,59 -> 500,56 -> 500,59 -> 502,59 -> 502,56 -> 502,59 -> 504,59 -> 504,57 -> 504,59
507,43 -> 511,43
523,100 -> 523,104 -> 519,104 -> 519,111 -> 528,111 -> 528,104 -> 526,104 -> 526,100
506,31 -> 511,31
497,72 -> 497,63 -> 497,72 -> 499,72 -> 499,65 -> 499,72 -> 501,72 -> 501,71 -> 501,72 -> 503,72 -> 503,67 -> 503,72 -> 505,72 -> 505,63 -> 505,72 -> 507,72 -> 507,71 -> 507,72 -> 509,72 -> 509,70 -> 509,72 -> 511,72 -> 511,69 -> 511,72
510,75 -> 510,78 -> 505,78 -> 505,84 -> 518,84 -> 518,78 -> 515,78 -> 515,75
513,97 -> 513,89 -> 513,97 -> 515,97 -> 515,94 -> 515,97 -> 517,97 -> 517,94 -> 517,97 -> 519,97 -> 519,93 -> 519,97 -> 521,97 -> 521,88 -> 521,97 -> 523,97 -> 523,94 -> 523,97
507,117 -> 521,117 -> 521,116
488,59 -> 488,51 -> 488,59 -> 490,59 -> 490,52 -> 490,59 -> 492,59 -> 492,55 -> 492,59 -> 494,59 -> 494,52 -> 494,59 -> 496,59 -> 496,50 -> 496,59 -> 498,59 -> 498,58 -> 498,59 -> 500,59 -> 500,56 -> 500,59 -> 502,59 -> 502,56 -> 502,59 -> 504,59 -> 504,57 -> 504,59
534,152 -> 539,152
513,97 -> 513,89 -> 513,97 -> 515,97 -> 515,94 -> 515,97 -> 517,97 -> 517,94 -> 517,97 -> 519,97 -> 519,93 -> 519,97 -> 521,97 -> 521,88 -> 521,97 -> 523,97 -> 523,94 -> 523,97
488,59 -> 488,51 -> 488,59 -> 490,59 -> 490,52 -> 490,59 -> 492,59 -> 492,55 -> 492,59 -> 494,59 -> 494,52 -> 494,59 -> 496,59 -> 496,50 -> 496,59 -> 498,59 -> 498,58 -> 498,59 -> 500,59 -> 500,56 -> 500,59 -> 502,59 -> 502,56 -> 502,59 -> 504,59 -> 504,57 -> 504,59
497,72 -> 497,63 -> 497,72 -> 499,72 -> 499,65 -> 499,72 -> 501,72 -> 501,71 -> 501,72 -> 503,72 -> 503,67 -> 503,72 -> 505,72 -> 505,63 -> 505,72 -> 507,72 -> 507,71 -> 507,72 -> 509,72 -> 509,70 -> 509,72 -> 511,72 -> 511,69 -> 511,72
495,19 -> 500,19
488,59 -> 488,51 -> 488,59 -> 490,59 -> 490,52 -> 490,59 -> 492,59 -> 492,55 -> 492,59 -> 494,59 -> 494,52 -> 494,59 -> 496,59 -> 496,50 -> 496,59 -> 498,59 -> 498,58 -> 498,59 -> 500,59 -> 500,56 -> 500,59 -> 502,59 -> 502,56 -> 502,59 -> 504,59 -> 504,57 -> 504,59
513,97 -> 513,89 -> 513,97 -> 515,97 -> 515,94 -> 515,97 -> 517,97 -> 517,94 -> 517,97 -> 519,97 -> 519,93 -> 519,97 -> 521,97 -> 521,88 -> 521,97 -> 523,97 -> 523,94 -> 523,97
497,72 -> 497,63 -> 497,72 -> 499,72 -> 499,65 -> 499,72 -> 501,72 -> 501,71 -> 501,72 -> 503,72 -> 503,67 -> 503,72 -> 505,72 -> 505,63 -> 505,72 -> 507,72 -> 507,71 -> 507,72 -> 509,72 -> 509,70 -> 509,72 -> 511,72 -> 511,69 -> 511,72
513,97 -> 513,89 -> 513,97 -> 515,97 -> 515,94 -> 515,97 -> 517,97 -> 517,94 -> 517,97 -> 519,97 -> 519,93 -> 519,97 -> 521,97 -> 521,88 -> 521,97 -> 523,97 -> 523,94 -> 523,97
523,136 -> 523,138 -> 518,138 -> 518,145 -> 535,145 -> 535,138 -> 528,138 -> 528,136
497,72 -> 497,63 -> 497,72 -> 499,72 -> 499,65 -> 499,72 -> 501,72 -> 501,71 -> 501,72 -> 503,72 -> 503,67 -> 503,72 -> 505,72 -> 505,63 -> 505,72 -> 507,72 -> 507,71 -> 507,72 -> 509,72 -> 509,70 -> 509,72 -> 511,72 -> 511,69 -> 511,72
527,152 -> 532,152
513,97 -> 513,89 -> 513,97 -> 515,97 -> 515,94 -> 515,97 -> 517,97 -> 517,94 -> 517,97 -> 519,97 -> 519,93 -> 519,97 -> 521,97 -> 521,88 -> 521,97 -> 523,97 -> 523,94 -> 523,97
533,148 -> 538,148
510,75 -> 510,78 -> 505,78 -> 505,84 -> 518,84 -> 518,78 -> 515,78 -> 515,75
488,59 -> 488,51 -> 488,59 -> 490,59 -> 490,52 -> 490,59 -> 492,59 -> 492,55 -> 492,59 -> 494,59 -> 494,52 -> 494,59 -> 496,59 -> 496,50 -> 496,59 -> 498,59 -> 498,58 -> 498,59 -> 500,59 -> 500,56 -> 500,59 -> 502,59 -> 502,56 -> 502,59 -> 504,59 -> 504,57 -> 504,59
516,46 -> 520,46
497,72 -> 497,63 -> 497,72 -> 499,72 -> 499,65 -> 499,72 -> 501,72 -> 501,71 -> 501,72 -> 503,72 -> 503,67 -> 503,72 -> 505,72 -> 505,63 -> 505,72 -> 507,72 -> 507,71 -> 507,72 -> 509,72 -> 509,70 -> 509,72 -> 511,72 -> 511,69 -> 511,72
497,72 -> 497,63 -> 497,72 -> 499,72 -> 499,65 -> 499,72 -> 501,72 -> 501,71 -> 501,72 -> 503,72 -> 503,67 -> 503,72 -> 505,72 -> 505,63 -> 505,72 -> 507,72 -> 507,71 -> 507,72 -> 509,72 -> 509,70 -> 509,72 -> 511,72 -> 511,69 -> 511,72
545,155 -> 545,157 -> 541,157 -> 541,160 -> 556,160 -> 556,157 -> 550,157 -> 550,155
501,119 -> 501,120 -> 512,120 -> 512,119
513,97 -> 513,89 -> 513,97 -> 515,97 -> 515,94 -> 515,97 -> 517,97 -> 517,94 -> 517,97 -> 519,97 -> 519,93 -> 519,97 -> 521,97 -> 521,88 -> 521,97 -> 523,97 -> 523,94 -> 523,97
488,59 -> 488,51 -> 488,59 -> 490,59 -> 490,52 -> 490,59 -> 492,59 -> 492,55 -> 492,59 -> 494,59 -> 494,52 -> 494,59 -> 496,59 -> 496,50 -> 496,59 -> 498,59 -> 498,58 -> 498,59 -> 500,59 -> 500,56 -> 500,59 -> 502,59 -> 502,56 -> 502,59 -> 504,59 -> 504,57 -> 504,59
512,25 -> 517,25
526,133 -> 526,131 -> 526,133 -> 528,133 -> 528,129 -> 528,133 -> 530,133 -> 530,129 -> 530,133
497,72 -> 497,63 -> 497,72 -> 499,72 -> 499,65 -> 499,72 -> 501,72 -> 501,71 -> 501,72 -> 503,72 -> 503,67 -> 503,72 -> 505,72 -> 505,63 -> 505,72 -> 507,72 -> 507,71 -> 507,72 -> 509,72 -> 509,70 -> 509,72 -> 511,72 -> 511,69 -> 511,72
513,97 -> 513,89 -> 513,97 -> 515,97 -> 515,94 -> 515,97 -> 517,97 -> 517,94 -> 517,97 -> 519,97 -> 519,93 -> 519,97 -> 521,97 -> 521,88 -> 521,97 -> 523,97 -> 523,94 -> 523,97
513,31 -> 518,31
526,133 -> 526,131 -> 526,133 -> 528,133 -> 528,129 -> 528,133 -> 530,133 -> 530,129 -> 530,133
488,59 -> 488,51 -> 488,59 -> 490,59 -> 490,52 -> 490,59 -> 492,59 -> 492,55 -> 492,59 -> 494,59 -> 494,52 -> 494,59 -> 496,59 -> 496,50 -> 496,59 -> 498,59 -> 498,58 -> 498,59 -> 500,59 -> 500,56 -> 500,59 -> 502,59 -> 502,56 -> 502,59 -> 504,59 -> 504,57 -> 504,59
520,31 -> 525,31
497,72 -> 497,63 -> 497,72 -> 499,72 -> 499,65 -> 499,72 -> 501,72 -> 501,71 -> 501,72 -> 503,72 -> 503,67 -> 503,72 -> 505,72 -> 505,63 -> 505,72 -> 507,72 -> 507,71 -> 507,72 -> 509,72 -> 509,70 -> 509,72 -> 511,72 -> 511,69 -> 511,72
482,21 -> 482,22 -> 490,22
510,75 -> 510,78 -> 505,78 -> 505,84 -> 518,84 -> 518,78 -> 515,78 -> 515,75
498,17 -> 503,17
523,136 -> 523,138 -> 518,138 -> 518,145 -> 535,145 -> 535,138 -> 528,138 -> 528,136
545,155 -> 545,157 -> 541,157 -> 541,160 -> 556,160 -> 556,157 -> 550,157 -> 550,155
513,43 -> 517,43
523,100 -> 523,104 -> 519,104 -> 519,111 -> 528,111 -> 528,104 -> 526,104 -> 526,100
505,17 -> 510,17
497,72 -> 497,63 -> 497,72 -> 499,72 -> 499,65 -> 499,72 -> 501,72 -> 501,71 -> 501,72 -> 503,72 -> 503,67 -> 503,72 -> 505,72 -> 505,63 -> 505,72 -> 507,72 -> 507,71 -> 507,72 -> 509,72 -> 509,70 -> 509,72 -> 511,72 -> 511,69 -> 511,72
509,28 -> 514,28
545,155 -> 545,157 -> 541,157 -> 541,160 -> 556,160 -> 556,157 -> 550,157 -> 550,155
537,150 -> 542,150
497,72 -> 497,63 -> 497,72 -> 499,72 -> 499,65 -> 499,72 -> 501,72 -> 501,71 -> 501,72 -> 503,72 -> 503,67 -> 503,72 -> 505,72 -> 505,63 -> 505,72 -> 507,72 -> 507,71 -> 507,72 -> 509,72 -> 509,70 -> 509,72 -> 511,72 -> 511,69 -> 511,72
501,43 -> 505,43
513,97 -> 513,89 -> 513,97 -> 515,97 -> 515,94 -> 515,97 -> 517,97 -> 517,94 -> 517,97 -> 519,97 -> 519,93 -> 519,97 -> 521,97 -> 521,88 -> 521,97 -> 523,97 -> 523,94 -> 523,97
488,59 -> 488,51 -> 488,59 -> 490,59 -> 490,52 -> 490,59 -> 492,59 -> 492,55 -> 492,59 -> 494,59 -> 494,52 -> 494,59 -> 496,59 -> 496,50 -> 496,59 -> 498,59 -> 498,58 -> 498,59 -> 500,59 -> 500,56 -> 500,59 -> 502,59 -> 502,56 -> 502,59 -> 504,59 -> 504,57 -> 504,59
497,72 -> 497,63 -> 497,72 -> 499,72 -> 499,65 -> 499,72 -> 501,72 -> 501,71 -> 501,72 -> 503,72 -> 503,67 -> 503,72 -> 505,72 -> 505,63 -> 505,72 -> 507,72 -> 507,71 -> 507,72 -> 509,72 -> 509,70 -> 509,72 -> 511,72 -> 511,69 -> 511,72
501,119 -> 501,120 -> 512,120 -> 512,119
507,37 -> 511,37
497,72 -> 497,63 -> 497,72 -> 499,72 -> 499,65 -> 499,72 -> 501,72 -> 501,71 -> 501,72 -> 503,72 -> 503,67 -> 503,72 -> 505,72 -> 505,63 -> 505,72 -> 507,72 -> 507,71 -> 507,72 -> 509,72 -> 509,70 -> 509,72 -> 511,72 -> 511,69 -> 511,72
488,59 -> 488,51 -> 488,59 -> 490,59 -> 490,52 -> 490,59 -> 492,59 -> 492,55 -> 492,59 -> 494,59 -> 494,52 -> 494,59 -> 496,59 -> 496,50 -> 496,59 -> 498,59 -> 498,58 -> 498,59 -> 500,59 -> 500,56 -> 500,59 -> 502,59 -> 502,56 -> 502,59 -> 504,59 -> 504,57 -> 504,59
523,100 -> 523,104 -> 519,104 -> 519,111 -> 528,111 -> 528,104 -> 526,104 -> 526,100
488,59 -> 488,51 -> 488,59 -> 490,59 -> 490,52 -> 490,59 -> 492,59 -> 492,55 -> 492,59 -> 494,59 -> 494,52 -> 494,59 -> 496,59 -> 496,50 -> 496,59 -> 498,59 -> 498,58 -> 498,59 -> 500,59 -> 500,56 -> 500,59 -> 502,59 -> 502,56 -> 502,59 -> 504,59 -> 504,57 -> 504,59
513,97 -> 513,89 -> 513,97 -> 515,97 -> 515,94 -> 515,97 -> 517,97 -> 517,94 -> 517,97 -> 519,97 -> 519,93 -> 519,97 -> 521,97 -> 521,88 -> 521,97 -> 523,97 -> 523,94 -> 523,97
501,119 -> 501,120 -> 512,120 -> 512,119
510,40 -> 514,40
488,19 -> 493,19
482,21 -> 482,22 -> 490,22
494,15 -> 499,15
523,136 -> 523,138 -> 518,138 -> 518,145 -> 535,145 -> 535,138 -> 528,138 -> 528,136
526,133 -> 526,131 -> 526,133 -> 528,133 -> 528,129 -> 528,133 -> 530,133 -> 530,129 -> 530,133
513,97 -> 513,89 -> 513,97 -> 515,97 -> 515,94 -> 515,97 -> 517,97 -> 517,94 -> 517,97 -> 519,97 -> 519,93 -> 519,97 -> 521,97 -> 521,88 -> 521,97 -> 523,97 -> 523,94 -> 523,97
488,59 -> 488,51 -> 488,59 -> 490,59 -> 490,52 -> 490,59 -> 492,59 -> 492,55 -> 492,59 -> 494,59 -> 494,52 -> 494,59 -> 496,59 -> 496,50 -> 496,59 -> 498,59 -> 498,58 -> 498,59 -> 500,59 -> 500,56 -> 500,59 -> 502,59 -> 502,56 -> 502,59 -> 504,59 -> 504,57 -> 504,59
488,59 -> 488,51 -> 488,59 -> 490,59 -> 490,52 -> 490,59 -> 492,59 -> 492,55 -> 492,59 -> 494,59 -> 494,52 -> 494,59 -> 496,59 -> 496,50 -> 496,59 -> 498,59 -> 498,58 -> 498,59 -> 500,59 -> 500,56 -> 500,59 -> 502,59 -> 502,56 -> 502,59 -> 504,59 -> 504,57 -> 504,59
488,59 -> 488,51 -> 488,59 -> 490,59 -> 490,52 -> 490,59 -> 492,59 -> 492,55 -> 492,59 -> 494,59 -> 494,52 -> 494,59 -> 496,59 -> 496,50 -> 496,59 -> 498,59 -> 498,58 -> 498,59 -> 500,59 -> 500,56 -> 500,59 -> 502,59 -> 502,56 -> 502,59 -> 504,59 -> 504,57 -> 504,59
526,133 -> 526,131 -> 526,133 -> 528,133 -> 528,129 -> 528,133 -> 530,133 -> 530,129 -> 530,133
513,97 -> 513,89 -> 513,97 -> 515,97 -> 515,94 -> 515,97 -> 517,97 -> 517,94 -> 517,97 -> 519,97 -> 519,93 -> 519,97 -> 521,97 -> 521,88 -> 521,97 -> 523,97 -> 523,94 -> 523,97
488,59 -> 488,51 -> 488,59 -> 490,59 -> 490,52 -> 490,59 -> 492,59 -> 492,55 -> 492,59 -> 494,59 -> 494,52 -> 494,59 -> 496,59 -> 496,50 -> 496,59 -> 498,59 -> 498,58 -> 498,59 -> 500,59 -> 500,56 -> 500,59 -> 502,59 -> 502,56 -> 502,59 -> 504,59 -> 504,57 -> 504,59
545,155 -> 545,157 -> 541,157 -> 541,160 -> 556,160 -> 556,157 -> 550,157 -> 550,155
513,97 -> 513,89 -> 513,97 -> 515,97 -> 515,94 -> 515,97 -> 517,97 -> 517,94 -> 517,97 -> 519,97 -> 519,93 -> 519,97 -> 521,97 -> 521,88 -> 521,97 -> 523,97 -> 523,94 -> 523,97
523,136 -> 523,138 -> 518,138 -> 518,145 -> 535,145 -> 535,138 -> 528,138 -> 528,136
504,40 -> 508,40
497,72 -> 497,63 -> 497,72 -> 499,72 -> 499,65 -> 499,72 -> 501,72 -> 501,71 -> 501,72 -> 503,72 -> 503,67 -> 503,72 -> 505,72 -> 505,63 -> 505,72 -> 507,72 -> 507,71 -> 507,72 -> 509,72 -> 509,70 -> 509,72 -> 511,72 -> 511,69 -> 511,72
526,133 -> 526,131 -> 526,133 -> 528,133 -> 528,129 -> 528,133 -> 530,133 -> 530,129 -> 530,133
523,100 -> 523,104 -> 519,104 -> 519,111 -> 528,111 -> 528,104 -> 526,104 -> 526,100
545,155 -> 545,157 -> 541,157 -> 541,160 -> 556,160 -> 556,157 -> 550,157 -> 550,155
516,28 -> 521,28
545,155 -> 545,157 -> 541,157 -> 541,160 -> 556,160 -> 556,157 -> 550,157 -> 550,155
497,72 -> 497,63 -> 497,72 -> 499,72 -> 499,65 -> 499,72 -> 501,72 -> 501,71 -> 501,72 -> 503,72 -> 503,67 -> 503,72 -> 505,72 -> 505,63 -> 505,72 -> 507,72 -> 507,71 -> 507,72 -> 509,72 -> 509,70 -> 509,72 -> 511,72 -> 511,69 -> 511,72
497,72 -> 497,63 -> 497,72 -> 499,72 -> 499,65 -> 499,72 -> 501,72 -> 501,71 -> 501,72 -> 503,72 -> 503,67 -> 503,72 -> 505,72 -> 505,63 -> 505,72 -> 507,72 -> 507,71 -> 507,72 -> 509,72 -> 509,70 -> 509,72 -> 511,72 -> 511,69 -> 511,72
488,59 -> 488,51 -> 488,59 -> 490,59 -> 490,52 -> 490,59 -> 492,59 -> 492,55 -> 492,59 -> 494,59 -> 494,52 -> 494,59 -> 496,59 -> 496,50 -> 496,59 -> 498,59 -> 498,58 -> 498,59 -> 500,59 -> 500,56 -> 500,59 -> 502,59 -> 502,56 -> 502,59 -> 504,59 -> 504,57 -> 504,59
491,17 -> 496,17
488,59 -> 488,51 -> 488,59 -> 490,59 -> 490,52 -> 490,59 -> 492,59 -> 492,55 -> 492,59 -> 494,59 -> 494,52 -> 494,59 -> 496,59 -> 496,50 -> 496,59 -> 498,59 -> 498,58 -> 498,59 -> 500,59 -> 500,56 -> 500,59 -> 502,59 -> 502,56 -> 502,59 -> 504,59 -> 504,57 -> 504,59
526,133 -> 526,131 -> 526,133 -> 528,133 -> 528,129 -> 528,133 -> 530,133 -> 530,129 -> 530,133
497,72 -> 497,63 -> 497,72 -> 499,72 -> 499,65 -> 499,72 -> 501,72 -> 501,71 -> 501,72 -> 503,72 -> 503,67 -> 503,72 -> 505,72 -> 505,63 -> 505,72 -> 507,72 -> 507,71 -> 507,72 -> 509,72 -> 509,70 -> 509,72 -> 511,72 -> 511,69 -> 511,72
497,72 -> 497,63 -> 497,72 -> 499,72 -> 499,65 -> 499,72 -> 501,72 -> 501,71 -> 501,72 -> 503,72 -> 503,67 -> 503,72 -> 505,72 -> 505,63 -> 505,72 -> 507,72 -> 507,71 -> 507,72 -> 509,72 -> 509,70 -> 509,72 -> 511,72 -> 511,69 -> 511,72
523,100 -> 523,104 -> 519,104 -> 519,111 -> 528,111 -> 528,104 -> 526,104 -> 526,100
513,113 -> 513,114 -> 527,114
488,59 -> 488,51 -> 488,59 -> 490,59 -> 490,52 -> 490,59 -> 492,59 -> 492,55 -> 492,59 -> 494,59 -> 494,52 -> 494,59 -> 496,59 -> 496,50 -> 496,59 -> 498,59 -> 498,58 -> 498,59 -> 500,59 -> 500,56 -> 500,59 -> 502,59 -> 502,56 -> 502,59 -> 504,59 -> 504,57 -> 504,59
510,75 -> 510,78 -> 505,78 -> 505,84 -> 518,84 -> 518,78 -> 515,78 -> 515,75
488,59 -> 488,51 -> 488,59 -> 490,59 -> 490,52 -> 490,59 -> 492,59 -> 492,55 -> 492,59 -> 494,59 -> 494,52 -> 494,59 -> 496,59 -> 496,50 -> 496,59 -> 498,59 -> 498,58 -> 498,59 -> 500,59 -> 500,56 -> 500,59 -> 502,59 -> 502,56 -> 502,59 -> 504,59 -> 504,57 -> 504,59
541,152 -> 546,152
523,136 -> 523,138 -> 518,138 -> 518,145 -> 535,145 -> 535,138 -> 528,138 -> 528,136
""".parselines.map { it.split("->").map {
    it.trim().split(",").let {
        it[0].toInt() to it[1].toInt()
    }
}}

// test case

val tsandSource = 500 to 0
val trockGrid = testInput.toRockGrid(tsandSource)
trockGrid.print
trockGrid.pourSand()
trockGrid.print

val trockGrid2 = testInput.toRockGrid(tsandSource, floor = true)
trockGrid2.print
trockGrid2.pourSand()
trockGrid2.print

val testResult = trockGrid.count('o')
val testResult2 = trockGrid2.count('o')

fun List<List<Coord>>.toRockGrid(source: Coord, floor: Boolean = false): RockGrid {
    val minx = minOf { it.minOf { it.first }}.coerceAtMost(source.first)
    val miny = minOf { it.minOf { it.second }}.coerceAtMost(source.second)
    val maxx = maxOf { it.maxOf { it.first }}.coerceAtLeast(source.first)
    val maxy = maxOf { it.maxOf { it.second }}.coerceAtLeast(source.second)
    val grid = RockGrid(minx-1..maxx+1, miny-1..maxy+1, source, floor)
    forEach { path ->
        path.drop(1).forEachIndexed { i, pair ->
            val prev = path[i]
            if (prev.first == pair.first) {
                range(prev.second, pair.second).forEach { grid.rock(pair.first, it) }
            } else if (prev.second == pair.second) {
                range(prev.first, pair.first).forEach { grid.rock(it, pair.second) }
            }
        }
    }
    return grid
}

fun range(a: Int, b: Int) = if (a > b) b..a else a..b

class RockGrid(xr: IntRange, yr: IntRange, val source: Coord, floor: Boolean) {
    var xvals: IntRange
    var yvals: IntRange
    init {
        if (floor) {
            yvals = yr.first..yr.last+1
            val ht = yvals.last - yvals.first
            val minx = xr.first.coerceAtMost(source.first - ht)
            val maxx = xr.last.coerceAtLeast(source.first + ht)
            xvals = minx..maxx
        } else {
            xvals = xr
            yvals = yr
        }
    }
    val minx: Int = xvals.first
    val miny: Int = yvals.first
    val grid = yvals.map { xvals.map { '.' }.toMutableList() }
    init {
        grid[source.second-miny][source.first-minx] = '+'
        if (floor) {
            xvals.forEach { rock(it, yvals.last) }
        }
    }

    fun rock(x: Int, y: Int) { grid[y-miny][x-minx] = '#' }
    fun sand(x: Int, y: Int) { grid[y-miny][x-minx] = 'o' }

    fun count(c: Char) = grid.sumOf { it.count { it == c } }

    fun airat(x: Int, y: Int) = grid[y-miny][x-minx] in arrayOf('.','+')

    fun pourSand() {
        var res: Boolean
        do {
            res = pourGrainOfSand()
//            this.print
        } while (res)
    }

    fun pourGrainOfSand(from: Coord = source): Boolean {
        if (!airat(from.first, from.second)) return false
        // location of first rock or obstruction below
        val down = (from.second..yvals.last).firstOrNull {
            !airat(from.first, it)
        } ?: return false
        if (airat(from.first-1, down))
            pourGrainOfSand(from.first-1 to down).let { if (!it) return false }
        else if (airat(from.first+1, down))
            pourGrainOfSand(from.first+1 to down).let { if (!it) return false }
        else
            sand(from.first, down-1)
        return true
    }

    override fun toString() = grid.joinToString("\n") { String(it.toCharArray()) }
}

// part 1

val sandSource = 500 to 0
val rockGrid = input.toRockGrid(sandSource)
rockGrid.pourSand()

val answer1 = rockGrid.count('o')
answer1.print

// part 2

val rockGrid2 = input.toRockGrid(sandSource, floor = true)
rockGrid2.pourSand()
val answer2 = rockGrid2.count('o')
answer2.print

// print results

AocRunner(day,
    info = { listOf("Leaderboard: 10:33/13:54", "Answers: 901/24589") },
    test = { "$testResult, $testResult2" },
    part1 = { answer1 },
    part2 = { answer2 }
).run()