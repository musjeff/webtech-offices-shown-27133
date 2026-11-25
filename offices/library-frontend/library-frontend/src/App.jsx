import { useState } from 'react'
import heroImg from './assets/hero.png'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import './App.css'
import LibraryButton from './assets/components/LibraryButton'
import LibraryBooksList from './assets/components/LibraryBooksList'
import LibraryHeader from './assets/components/LibraryHeader'
import LibraryFooter from './assets/components/LibraryFooter'
import LibraryLogin from './assets/components/LibraryLogin'
import Office from './assets/components/Office'



function App() {
  const libraryName = "library management system"

  return (
   <>
     <div>
      <LibraryHeader />
      <main>
       {/* <LibraryBooksList />
        <LibraryButton />
       <LibraryFooter />
       <LibraryButton/> */}
       <Office/>
      </main>
     </div>

     {/* <LibraryLogin /> */}
     
     </>

  );
}

export default App
