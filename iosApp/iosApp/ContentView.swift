import SwiftUI
import shared
import Observation

@MainActor
final class ZipCodeViewModelWrapper: ObservableObject {
    private let viewModel = ZipCodeViewModel()
    private var task: Task<Void, Never>?
    
    @Published var address: AddressData? = nil
    @Published var isLoading: Bool = false
    @Published var error: String? = nil
    
    init() {
        startObserving()
    }
    
    deinit {
        task?.cancel()
    }
    
    private func startObserving() {
        task = Task { [weak self] in
            while !Task.isCancelled {
                self?.updateState()
                try? await Task.sleep(nanoseconds: 100_000_000)
            }
        }
    }
    
    private func updateState() {
        self.address = viewModel.currentAddress
        self.isLoading = viewModel.currentIsLoading
        self.error = viewModel.currentError
    }
    
    func searchZipCode(zipCode: String) {
        viewModel.searchZipCode(zipCode: zipCode)
    }
}

struct ContentView: View {
    @StateObject private var viewModel = ZipCodeViewModelWrapper()
    @State private var zipCode: String = ""
    
    var body: some View {
        VStack(spacing: 16) {
            TextField("郵便番号", text: $zipCode)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .keyboardType(.numberPad)
                .onChange(of: zipCode) { newValue in
                    let filtered = newValue.filter { "0123456789".contains($0) }
                    if filtered != newValue {
                        zipCode = filtered
                    }
                    if zipCode.count > 7 {
                        zipCode = String(zipCode.prefix(7))
                    }
                }
                .padding(.horizontal)
            
            Button(action: {
                viewModel.searchZipCode(zipCode: zipCode)
            }) {
                Text("検索")
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(Color.blue)
                    .foregroundColor(.white)
                    .cornerRadius(8)
            }
            .padding(.horizontal)
            .disabled(zipCode.count != 7)
            
            if viewModel.isLoading {
                ProgressView()
            }
            
            if let error = viewModel.error {
                Text(error)
                    .foregroundColor(.red)
                    .padding()
            }
            
            if let address = viewModel.address {
                VStack(alignment: .leading, spacing: 8) {
                    Text("都道府県: \(address.prefecture)")
                    Text("市区町村: \(address.city)")
                    Text("町名: \(address.town)")
                }
                .padding()
                .background(Color.gray.opacity(0.1))
                .cornerRadius(8)
                .padding(.horizontal)
            }
            
            Spacer()
        }
        .onTapGesture {
            UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), 
                                         to: nil, 
                                         from: nil, 
                                         for: nil)
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}