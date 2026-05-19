# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 1/5 (20.0%)
- **Function parity:** 20/98 matched (target 20) — 20.4%
- **Class/type parity:** 0/0 matched (target 1) — N/A
- **Combined symbol parity:** 20/98 matched (target 21) — 20.4%
- **Average inline-code cosine:** 0.29 (function body across 1 matched files)
- **Average documentation cosine:** 0.86 (doc text across 1 matched files)
- **Cheat-zeroed Files:** 0
- **Critical Issues:** 1 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. lib

- **Target:** `dirs.Lib`
- **Similarity:** 0.29
- **Dependents:** 0
- **Priority Score:** 2007.1
- **Functions:** 20/20 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Tests:** 1/1 matched

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

## Next Commands

```bash
# Initialize task queue for systematic porting
cd tools/ast_distance
./ast_distance --init-tasks ../../tmp/dirs/src rust ../../src/commonMain/kotlin/io/github/kotlinmania/dirs kotlin tasks.json ../../AGENTS.md

# Get next high-priority task
./ast_distance --assign tasks.json <agent-id>
```
