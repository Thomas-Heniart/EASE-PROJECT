import React from 'react';
import {Segment, Loader} from 'semantic-ui-react';

class ChromeSecondStep extends React.Component {
  render() {
    return (
      <React.Fragment>
        <Segment id='chromeSteps'>
          <p className='title'><img src="/resources/other/Chrome.png"/> Google Chrome is being imported</p>
          <div style={{margin:'45px 0'}}>
            <Loader active style={{position:'relative',transform:'translateX(-50%)',bottom:'50%'}}/>
          </div>
          <p>Ease.space integrates your accounts by finding them in a new tab. Please, do not close it.</p>
          <p>Once it’s done, you will be able to select what you keep on Ease.space.</p>
          <p>It will take approximately 45sec, be patient ;)</p>
        </Segment>
      </React.Fragment>
    )
  }
}

export default ChromeSecondStep;