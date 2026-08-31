import Testing
import Dirs

@Suite("Dirs Swift Export Smoke Tests")
struct DirsExportTests {
    @Test("Dirs swift module imported cleanly and returns valid paths")
    func swiftModuleLoads() {
        let home = Dirs.homeDir()
        #expect(home != nil)
        #expect(!home!.isEmpty)
    }
}

