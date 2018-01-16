import React, {Component} from "react";
import AddAnyApp from './AddAnyApp';
import { Grid, Image, Icon, Header, Input, Container, Loader } from 'semantic-ui-react';

const AppsContainer  = ({websites, title, openModal}) => {
  return (
      <Container fluid>
        <h3>
          {title}
        </h3>
        <Grid columns={4} className="logoCatalog">
          {websites.map((item) =>
              <Grid.Column key={item.id} as='a' className="showSegment" onClick={e => openModal(item)}>
                <Image src={item.logo}/>
                <p>{item.name}</p>
                <Icon name="add square"/>
              </Grid.Column>
          )}
        </Grid>
        <AddAnyApp query={""} focus={false}/>
      </Container>
  )
};

export default AppsContainer;