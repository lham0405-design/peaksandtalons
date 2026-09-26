# Static audit after Build 176

## Verified in source
- Chapter I uses one preparation hub with four sibling branches and a reconverging Expedition Essentials milestone.
- Orduk chapter preserves Explorer's Compass -> Hollow -> podium -> Orduk -> Heart of Orduk progression.
- Offering podium checks specifically for Explorer's Compass, rejects activation while nearby monsters remain, consumes the compass outside creative mode, and spawns Orduk.
- Heart of Orduk now has an explicit generated item model referencing `peaksandtalons:item/heart_of_orduk`.
- Orduk worldgen is registered through a jigsaw/template-pool path.

## Live verification still mandatory
- FTB dependency lines visibly render in the packaged client and support chapters do not show as Unnamed.
- Explorer's Compass successfully finds a generated Orduk cave in a fresh world/unexplored eligible chunks.
- Generated arena has intended flight volume, hostile eagles/spiders, and podium placement.
- Talonteer, Talonspire, chains, and Heart visuals meet the player's visual expectations.
- Orduk produces exactly one Heart and the Heart correctly transitions/tracks toward the Griffin.

A successful CI build closes compilation/static packaging only; it does not close the live items above.
